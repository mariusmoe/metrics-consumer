package com.moe.metricsconsumer.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.moe.metricsconsumer.apiErrorHandling.CouldNotSaveException;
import com.moe.metricsconsumer.apiErrorHandling.EntityNotFoundException;
import com.moe.metricsconsumer.models.measureSummary.MeasureSummary;
import com.moe.metricsconsumer.models.rewards.Achievement;
import com.moe.metricsconsumer.models.rewards.AchievementState;
import com.moe.metricsconsumer.models.rewards.UserAchievement;
import com.moe.metricsconsumer.repositories.AchievementRepository;
import com.moe.metricsconsumer.repositories.MeasureRepository;
import com.moe.metricsconsumer.repositories.UserAchievementRepository;
import no.hal.learning.fv.*;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.logging.*;
import java.util.stream.Collectors;

@RequestMapping("/api")
@Controller
public class MetricsController {

  @Autowired
  private MeasureRepository measureRepository;

  @Autowired
  private MongoTemplate mongoTemplate;

  @Autowired
  private UserAchievementRepository userAchievementRepository;

  @Autowired
  private AchievementRepository achievementRepository;

  @Autowired
  ObjectMapper mapper;

  ControllerUtil controllerUtil = new ControllerUtil();

  /**
   * Retrieve a list of available tasks. Details of measuresummaries are omitted to save bandwidth
   * @return a list where each task is only represented once
   */
  @GetMapping("/")
  @ResponseBody
  public List<MeasureSummary> getAllMeasureSummaries() {
    // Distinct is not implemented well in Spring and a set is used to remove duplicates
    // This problem is resolved in mongoclient 2.1.0 but the currently installed mongo springboot use 2.0.10
    Collection<MeasureSummary> shortList =  measureRepository.findAllByUserId("001")
        .stream()
        .collect(Collectors.toConcurrentMap(MeasureSummary::getTaskId, Function.identity(), (p, q) -> p))
        .values();
    return new ArrayList<>(shortList);
  }

  /**
   * Get the details of a measuresummary based on the selected taskId
   * @param taskId  The task to retrieve details of
   * @return MeasureSummary if found
   * @throws EntityNotFoundException
   */
  @GetMapping("/{taskId}")
  @ResponseBody
  public MeasureSummary getMeasureSummary(@NonNull @PathVariable("taskId") String taskId) throws EntityNotFoundException {
    return this.measureRepository.findFirstByUserIdAndTaskId("001", taskId)
      .orElseThrow(() -> new EntityNotFoundException(MeasureSummary.class, taskId));
  }

  /**
   * Retrieve the solution guide for the task with the provided taskId
   * @param taskId
   * @return solution MeasureSummary
   * @throws EntityNotFoundException
   */
  @GetMapping("/solution/{taskId}")
  @ResponseBody
  public MeasureSummary getSolutionMeasureSummary(@NonNull @PathVariable("taskId") String taskId) throws EntityNotFoundException {
    Query query = new Query();
    query.addCriteria(Criteria.where("isSolutionManual").is(true));
    query.addCriteria(Criteria.where("taskId").is(taskId));
//    TODO: use this and test it!!!
//    return this.measureRepository.findFirstIsSolutionManualAndTaskId(true, taskId);
    return this.mongoTemplate.findOne(query, MeasureSummary.class);
  }

  // TODO Add userid to the saved object
  /**
   * Save a new measuresummary for the current user
   * @param newMeasureSummary
   * @return
   */
  @PostMapping("/")
  @ResponseBody
  public ObjectNode newMeasureSummary(@Valid @RequestBody MeasureSummary newMeasureSummary) throws NoSuchFieldException, CouldNotSaveException {
    System.out.println("********************START************************\n\n");
    MeasureSummary measureSummary = newMeasureSummary;
    measureSummary.setId(getUserIdAndTaskNameHashed(measureSummary));
    // Get all achievements for this task   |\
    // Get all cumulative achievements      | \ -> could these be done with one request?
    List<Achievement> relevantAchievements = this.achievementRepository.findByTaskIdRefOrIsCumulative(measureSummary.getTaskId(), true);
    // Will find all achievements a user has ever received
    List<UserAchievement> userAchievements = this.userAchievementRepository.findAllByUserRef(measureSummary.getUserId());
    FeatureValuedContainer featureValuedContainer = controllerUtil.createContainerFromMeasures(measureSummary);
    // Loop over -> add achieved achievements to list as a list of UserAchievement
    List<UserAchievement> userAchievementList = new ArrayList<>();
    for (Achievement achievement : relevantAchievements){
      System.out.println("--------------------------------------------");
      // Get the expression as a resource for the provided achievement
      Resource resource = getResource(achievement);
      // If it is cumulative we should replace/add the value for this task or create it
      if (achievement.isCumulative()) {
        UserAchievement userAchievement = userAchievements.stream()
          .filter(object -> achievement.getId().equals(object.getAchievementRef())).findAny().orElse(null);
        FeatureList calculatedFeatureList = getCalculatedFeatureList(featureValuedContainer, resource);
        if (userAchievement != null) {
          // It exists already -> just update or add to history
          userAchievement.getHistory().put(measureSummary.getTaskName(),calculatedFeatureList.getFeatures().size());
          userAchievementList.add(userAchievement);
        } else {
          // It ain't here -> create a new UserAchievement
          Map<String, Integer> newHistory = new HashMap<>();
          newHistory.put(measureSummary.getTaskName(),calculatedFeatureList.getFeatures().size() );
          UserAchievement newUserAchievement = new UserAchievement(measureSummary.getUserId(),
            achievement.getId(),
            AchievementState.REVEALED, LocalDateTime.now(), newHistory);
          userAchievementList.add(newUserAchievement);
        }
      }
      else if (achievement.getTaskIdRef().equals(measureSummary.getTaskId())) {
        // The achievement belong to this task -> Create/overwrite userAchievement
        ConfigCreator configCreator = new ConfigCreator();
        // Load config from file system
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
        FeatureList calculatedFeatureList = getCalculatedFeatureList(featureValuedContainer, resource);
        // Printed too many times due to all achivements has the same config atm
        System.out.println("CalculatedFeatureList: " + calculatedFeatureList);
        // Eval method for if achievement should be given
        if (calculatedFeatureList.getFeatures().size() > 0) {
          System.out.println("GRATULERER!   -   CalculatedFeatureList: " + calculatedFeatureList);
          UserAchievement newUserAchievement = new UserAchievement(measureSummary.getUserId(),
            achievement.getId(),
            AchievementState.UNLOCKED, LocalDateTime.now(), null);
          userAchievementList.add(newUserAchievement);
        }
      }
    };
    System.out.println("******************END*************************\n\n");
    System.out.println("userAchievementList: " + userAchievementList);
    // Batch save the achieved achievements
    this.userAchievementRepository.saveAll(userAchievementList);

    return saveMeasureSummary(measureSummary);
  }

  /**
   * Combines the user id and task name then hashes it
   * @param measureSummary  The whole measuresummary
   * @return  the hashed name
   */
  private String getUserIdAndTaskNameHashed(MeasureSummary measureSummary) {
    StringBuilder stringBuilder = new StringBuilder();
    // TODO: use user id from principal instead of provided object
    stringBuilder.append(measureSummary.getUserId());
    stringBuilder.append(measureSummary.getTaskName());
    MessageDigest messageDigest = null;

    // Why hash the name? We don't want to share the id unnecessarily
    try {
      messageDigest = MessageDigest.getInstance("SHA-256");
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    messageDigest.update(stringBuilder.toString().getBytes());
    String hashedName = Base64.getEncoder().encodeToString(messageDigest.digest());
    return hashedName;
  }

  /**
   * Get the expression as a resource for the provided achievement
   * @param achievement to extract the expression from
   * @return Resource resource for the expression of the achievement
   */
  private Resource getResource(Achievement achievement) {
    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
    ResourceSet resSet = new ResourceSetImpl();

    // load config from XMI
    Resource configResource = resSet.createResource(URI.createURI("achievementConfig3.xmi"));
    Resource dataResource = resSet.createResource(URI.createURI("achievementData3.xmi"));
    try {
      configResource.load(new ByteArrayInputStream(achievement.getExpression().getData()),null );
      dataResource.load(new ByteArrayInputStream(achievement.getDummyData().getData()),null );
    } catch (IOException e) {
      e.printStackTrace();
    }
    return configResource;
  }

  /**
   * Converts the eObject with the replaced references to a featureList
   * @param featureValuedContainer  The container with the userdata
   * @param resource  the resource loaded from the achievement
   * @return a featureList representation of the fconstructed fv model
   */
  private FeatureList getCalculatedFeatureList(FeatureValuedContainer featureValuedContainer, Resource resource) {
    FeatureList calculatedFeatureList = FvFactory.eINSTANCE.createFeatureList();
    // Replace all references to 'other' in config.xmi with 'featureList'
    for (EObject eObject : resource.getContents()) {
      eObject = controllerUtil.replaceReference(eObject, featureValuedContainer);
      if (eObject instanceof FeatureValuedContainer){
        calculatedFeatureList.set(featureValuedContainerToFeatureList((FeatureValuedContainer) eObject), false);
      } else {
        calculatedFeatureList.set(((FeatureValued) eObject).toFeatureList(), false);
      }
    }
    return calculatedFeatureList;
  }

  /**
   * Helper method for converting containers to featureLists
   * @param featureValuedContainer
   * @return
   */
  private FeatureList featureValuedContainerToFeatureList(FeatureValuedContainer featureValuedContainer) {
    FeatureList featureList = FvFactory.eINSTANCE.createFeatureList();
    for (FeatureValued featureValued : featureValuedContainer.getFeatureValuedGroups()){
      featureList.set(featureValued.toFeatureList(), false);
    }
    return featureList;
  }

  /**
   * Create a userAchievement and sets the userRef to the userId param
   * @param achievement   The achievement to reward the user
   * @param userId        The user that shall receive the reward
   * @throws Exception
   */
  @Deprecated
  private void giveRewardToUser(Achievement achievement, String userId) throws CouldNotSaveException {
    UserAchievement userAchievement = new UserAchievement(
      userId, achievement.getId(), AchievementState.UNLOCKED, LocalDateTime.now(), null);
    System.out.println("userAchievement: " + userAchievement);
    try {
      this.userAchievementRepository.save(userAchievement);
    } catch (Exception ex) {
      throw new CouldNotSaveException(userAchievement.getClass(), userAchievement.toString());
    }
  }


  /**
   * Add a new SolutionMeasureSummary
   * @param newMeasureSummary
   * @return
   * @throws CouldNotSaveException
   */
  @PostMapping("/solution")
  @ResponseBody
  public ObjectNode newSolutionMeasureSummary(@Valid @RequestBody MeasureSummary newMeasureSummary) throws CouldNotSaveException{
    MeasureSummary measureSummary = newMeasureSummary;
    measureSummary.setSolutionManual(true);
    return saveMeasureSummary(measureSummary);
  }


  /**
   * Saves the provided measure summary
   * @param newMeasureSummary
   * @return
   * @throws CouldNotSaveException
   */
  public ObjectNode saveMeasureSummary(@RequestBody @Valid MeasureSummary newMeasureSummary) throws CouldNotSaveException {
    // TODO: convert to simple jsonResponse or skip special formatting?
    ObjectNode res;
    try {
      measureRepository.save(newMeasureSummary);
      res =  mapper.createObjectNode();
      res.put("measureSummaryRef", newMeasureSummary.getId());
      res.put("measureSummary", newMeasureSummary.toString());
      res.put("status", "2000");
    } catch (Exception e) {
      throw new CouldNotSaveException(newMeasureSummary.getClass(), newMeasureSummary.toString());
    }
    return res;
  }

  /**
   * Delete the measuresummary with the provided id
   * @param id id of measuresummary to delete
   * @return
   * @throws EntityNotFoundException
   */
  @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
  @ResponseBody
  public List<MeasureSummary> deleteMeasureSummary(@NonNull @PathVariable("id") String id) throws EntityNotFoundException {
    // TODO: check that the user has permission to do this action
    return this.measureRepository.removeById(id);
  }

  /**
   * Remove all measuresummaries for the currently logged in user
   * @return a list of the deleted data
   */
  @Deprecated
  @DeleteMapping("/all/delete")
  @ResponseBody
  public List<MeasureSummary> deleteMeasureSummary() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    LinkedHashMap linkedHashMap = (LinkedHashMap) auth.getPrincipal();
    return this.measureRepository.removeAllByUserId((String) linkedHashMap.get("userid"));
  }

}

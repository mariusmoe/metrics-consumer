package com.moe.metricsconsumer.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.moe.metricsconsumer.apiErrorHandling.EntityNotFoundException;
import com.moe.metricsconsumer.models.measureSummary.Measure;
import com.moe.metricsconsumer.models.measureSummary.MeasureSummary;
import com.moe.metricsconsumer.models.measureSummary.SpecificMeasure;
import com.moe.metricsconsumer.models.rewards.Achievement;
import com.moe.metricsconsumer.models.rewards.AchievementState;
import com.moe.metricsconsumer.models.rewards.UserAchievement;
import com.moe.metricsconsumer.repositories.AchievementRepository;
import com.moe.metricsconsumer.repositories.MeasureRepository;
import com.moe.metricsconsumer.repositories.UserAchievementRepository;
import no.hal.learning.fv.*;
import org.codehaus.jackson.JsonNode;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

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

  ControllerUtil controllerUtil = new ControllerUtil();



  @Autowired
  ObjectMapper mapper;

  @RequestMapping("/resource")
  @ResponseBody
  public Principal user(Principal principal) {
    return principal;
  }

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
    return this.measureRepository.findFirstByUserIdAndTaskId("001", taskId);
  }

  // TODO: add all solution routes to its own controller

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
  public ObjectNode newMeasureSummary(@Valid @RequestBody MeasureSummary newMeasureSummary) throws NoSuchFieldException {
    MeasureSummary measureSummary = newMeasureSummary;

    StringBuilder stringBuilder = new StringBuilder();
    // TODO: use user id from principal instead of provided object
    stringBuilder.append(newMeasureSummary.getUserId());
    stringBuilder.append(newMeasureSummary.getTaskName());
    MessageDigest messageDigest = null;

    // Why hash the name? We don't want to share the id unnecessarily
    try {
      messageDigest = MessageDigest.getInstance("SHA-256");
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    messageDigest.update(stringBuilder.toString().getBytes());
    String hashedName = Base64.getEncoder().encodeToString(messageDigest.digest());
    measureSummary.setId(hashedName);

    // TODO: Check if reward is warranted
    // Get all achievements for this task   |\
    // Get all cumulative achievements      | \ -> could these be done with one request?

    Criteria criteria = new Criteria();
    criteria.orOperator(
      Criteria.where("taskIdRef").is(measureSummary.getTaskId()),
      Criteria.where("isCumulative").is(true));
    Query query = new Query(criteria);
    List<Achievement> relevantAchievements = this.mongoTemplate.find(query, Achievement.class);

    // Will find all achievements a user has ever received

    System.out.println("newMeasureSummary.getUserId(): " + newMeasureSummary.getUserId());

    Query query2 = new Query(Criteria.where("userRef").is(newMeasureSummary.getUserId()));
    List<UserAchievement> userAchievements = this.mongoTemplate.find(query2, UserAchievement.class);

    System.out.println("relevantAchievements: " + relevantAchievements);
    System.out.println("userAchievements: " + userAchievements);

    FeatureValuedContainer featureValuedContainer = controllerUtil.createContainerFromMeasures(measureSummary);

    String userAchievementId = "";
    // Loop over -> add achieved achievements to list as a list of UserAchievement
    List<UserAchievement> userAchievementList = new ArrayList<>();
    for (Achievement achievement : relevantAchievements){

      // If it is cumulative we should replace/add the value for this task or create it
      if (achievement.isCumulative()) {
        UserAchievement userAchievement = userAchievements.stream()
          .filter(object -> achievement.getId().equals(object.getAchievementRef()))
          .findAny()
          .orElse(null);
        if (userAchievement != null) {
          // It exists already -> just update or add to history
//          userAchievement.getHistory().put(measureSummary.getTaskName(), 5);
//          userAchievementList.add(userAchievement);
        } else {
          // It ain't here -> create a new UserAchievement
          // TODO: create eval method for (if the reward should be given/progress)
          UserAchievement newUserAchievement = new UserAchievement(newMeasureSummary.getUserId(),
            achievement.getId(),
            AchievementState.REVEALED, null, null);
          userAchievementList.add(newUserAchievement);
        }
      }
      else if (achievement.getTaskIdRef().equals(measureSummary.getTaskId())) {
        // The achievement belong to this task -> Create/overwrite userAchievement
        // TODO: create eval method for (if the reward should be given)
        ConfigCreator configCreator = new ConfigCreator();
        try {
          configCreator.createAchievementConfig();
        } catch (IOException e) {
          e.printStackTrace();
        }
        // Load config from file system
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
        ResourceSet resSet = new ResourceSetImpl();
        Resource resource = resSet.getResource(URI.createURI("achievementConfig.xmi"), true);

        FeatureList calculatedFeatureList = FvFactory.eINSTANCE.createFeatureList();
        // Replace all references to 'other' in config.xmi with 'featureList'
        for (EObject eObject : resource.getContents()) {

          eObject = controllerUtil.replaceReference(eObject, featureValuedContainer);
          calculatedFeatureList = ((FeatureValued) eObject).toFeatureList();

        }
        // Printed too many times due to all achivements has the same config atm
        System.out.println("----------------------------");
        System.out.println("calculatedFeatureList: " + calculatedFeatureList);
        System.out.println("----------------------------");


        if (calculatedFeatureList.getFeatures().size() > 0) {

          System.out.println("GRATULERER!!!");
        }




        System.out.println("READ tHIS: " + calculatedFeatureList);


        UserAchievement newUserAchievement = new UserAchievement(newMeasureSummary.getUserId(),
          achievement.getId(),
          AchievementState.REVEALED, null, null);
        System.out.println("newUserAchievement: " + newUserAchievement);
        userAchievementList.add(newUserAchievement);


      }
    };

    System.out.println("userAchievementList: " + userAchievementList);
    // Batch save the achieved achievements
    this.userAchievementRepository.saveAll(userAchievementList);

    return saveMeasureSummary(measureSummary);

  }


  @PostMapping("/solution")
  @ResponseBody
  public ObjectNode newSolutionMeasureSummary(@Valid @RequestBody MeasureSummary newMeasureSummary){
    MeasureSummary measureSummary = newMeasureSummary;
    measureSummary.setSolutionManual(true);
    return saveMeasureSummary(measureSummary);
  }


  public ObjectNode saveMeasureSummary(@RequestBody @Valid MeasureSummary newMeasureSummary) {
    ObjectNode res;
    try {
      measureRepository.save(newMeasureSummary);
      res =  mapper.createObjectNode();
      res.put("measureSummaryRef", newMeasureSummary.getId());
      res.put("measureSummary", newMeasureSummary.toString());
      res.put("status", "2000");
    } catch (Exception e) {
      res =  mapper.createObjectNode();
      res.put("message", "ERROR: " + e.toString());
      res.put("status", "4000");
    }
    return res;
  }



  @GetMapping("/achievements/user")
  @ResponseBody
  public List<UserAchievement> getAllAchievedAchievements() {
    return this.userAchievementRepository.findAllByUserRef("001");
  }


  @GetMapping("/achievements")
  @ResponseBody
  public List<Achievement> getAllAchievements() {
    return this.achievementRepository.findAll();
  }


  @GetMapping("/mod")
  @ResponseBody
  public String getMessageOfTheDay(Principal principal) {
    return "The message of the day is boring for user: " + principal.getName();
  }

  @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
  @ResponseBody
  public List<MeasureSummary> deleteMeasureSummary(@NonNull @PathVariable("id") String id) throws EntityNotFoundException {
    return this.measureRepository.removeById(id);
  }

  @Deprecated
  @DeleteMapping("/all/delete")
  @ResponseBody
  public List<MeasureSummary> deleteMeasureSummary() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    LinkedHashMap linkedHashMap = (LinkedHashMap) auth.getPrincipal();
    return this.measureRepository.removeAllByUserId((String) linkedHashMap.get("userid"));
  }














}

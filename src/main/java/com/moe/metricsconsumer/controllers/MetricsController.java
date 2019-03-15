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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
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

  Logger logger = LoggerFactory.getLogger(MetricsController.class);

  /**
   * Retrieve a list of available tasks. Details of measuresummaries are omitted to save bandwidth
   * @return a list where each task is only represented once
   */
  @GetMapping("/")
  @ResponseBody
  public List<MeasureSummary> getAllMeasureSummaries(Principal principal) {
    // Distinct is not implemented well in Spring and a set is used to remove duplicates
    // This problem is resolved in mongoclient 2.1.0 but the currently installed mongo springboot use 2.0.10
    Map<String, String> principalMap = this.controllerUtil.getPrincipalAsMap(principal);
    Collection<MeasureSummary> shortList =  measureRepository.findAllByUserId(principalMap.get("userid"))
        .stream()
        .collect(Collectors.toConcurrentMap(MeasureSummary::getTaskId, Function.identity(), (p, q) -> p))
        .values();
    return new ArrayList<>(shortList);
  }

  /**
   * Get the details of a measuresummary based on the selected taskId
   * @param taskId  The task to retrieve details of
   * @return MeasureSummary if found
   * @throws EntityNotFoundException error for not found
   */
  @GetMapping("/{taskId}")
  @ResponseBody
  public MeasureSummary getMeasureSummary(@NonNull @PathVariable("taskId") String taskId, Principal principal) throws EntityNotFoundException {
    Map<String, String> principalMap = this.controllerUtil.getPrincipalAsMap(principal);
    return this.measureRepository.findFirstByUserIdAndTaskIdAndIsSolutionManual(principalMap.get("userid"), taskId, false)
      .orElseThrow(() -> new EntityNotFoundException(MeasureSummary.class, taskId));
  }

  /**
   * Retrieve the solution guide for the task with the provided taskId
   * @param taskId
   * @return solution MeasureSummary
   * @throws EntityNotFoundException  error for not found
   */
  @GetMapping("/solution/{taskId}")
  @ResponseBody
  public MeasureSummary getSolutionMeasureSummary(@NonNull @PathVariable("taskId") String taskId) throws EntityNotFoundException {
    Query query = new Query();
    query.addCriteria(Criteria.where("isSolutionManual").is(true));
    query.addCriteria(Criteria.where("taskId").is(taskId));
//    return this.measureRepository.findFirstIsSolutionManualAndTaskId(true, taskId);
    return this.mongoTemplate.findOne(query, MeasureSummary.class);
  }


  /**
   * Save a new measureSummary for the current user
   *
   * Also check if awards should be given, gives award per exercise and cumulative
   * @param newMeasureSummary The content of the new measureSummary to be saved
   * @return  what has been saved
   */
  @PostMapping("/")
  @ResponseBody
  public ObjectNode newMeasureSummary(@Valid @RequestBody MeasureSummary newMeasureSummary, Principal principal) throws NoSuchFieldException, CouldNotSaveException {
    MeasureSummary measureSummary = newMeasureSummary;
    measureSummary.setId(getUserIdAndTaskNameHashed(measureSummary, principal));
    // Get all achievements for this task   |\
    // Get all cumulative achievements      | \
    Map<String, String> principalMap = this.controllerUtil.getPrincipalAsMap(principal);
    List<Achievement> relevantAchievements = this.achievementRepository.findByTaskIdRefOrIsCumulative(measureSummary.getTaskId(), true);
    // Will find all achievements a user has ever received
    List<UserAchievement> userAchievements = this.userAchievementRepository.findAllByUserRef(principalMap.get("userid"));
    List<UserAchievement> rewardedUserAchievements = controllerUtil.checkAchievements(measureSummary,relevantAchievements, userAchievements);
    this.userAchievementRepository.saveAll(rewardedUserAchievements);
    logger.debug(rewardedUserAchievements.toString());
    return saveMeasureSummary(measureSummary, principal);
  }



  /**
   * Combines the user id and task name then hashes it
   * @param measureSummary  The whole measuresummary
   * @return  the hashed name
   */
  private String getUserIdAndTaskNameHashed(MeasureSummary measureSummary, Principal principal) {
    StringBuilder stringBuilder = new StringBuilder();

    Map<String, String> principalMap = this.controllerUtil.getPrincipalAsMap(principal);
    stringBuilder.append(principalMap.get("userid"));
    stringBuilder.append(measureSummary.getTaskName());
    MessageDigest messageDigest = null;

    // Why hash the name? We don't want to share the id unnecessarily
    try {
      messageDigest = MessageDigest.getInstance("SHA-256");
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    messageDigest.update(stringBuilder.toString().getBytes());
    return Base64.getEncoder().encodeToString(messageDigest.digest());
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
    logger.debug("userAchievement: " + userAchievement);
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
  public ObjectNode newSolutionMeasureSummary(@Valid @RequestBody MeasureSummary newMeasureSummary, Principal principal) throws CouldNotSaveException{

    MeasureSummary measureSummary = newMeasureSummary;
    measureSummary.setSolutionManual(true);
    return saveMeasureSummary(measureSummary, principal);
  }


  /**
   * Saves the provided measure summary
   * @param newMeasureSummary
   * @return
   * @throws CouldNotSaveException
   */
  public ObjectNode saveMeasureSummary(@RequestBody @Valid MeasureSummary newMeasureSummary, Principal principal) throws CouldNotSaveException {
    // TODO: convert to simple jsonResponse or skip special formatting?
    Map<String, String> principalMap = this.controllerUtil.getPrincipalAsMap(principal);
    newMeasureSummary.setUserId(principalMap.get("userid"));
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
  public List<MeasureSummary> deleteMeasureSummary(@NonNull @PathVariable("id") String id, Principal principal) throws EntityNotFoundException {
    Map<String, String> principalMap = this.controllerUtil.getPrincipalAsMap(principal);
    MeasureSummary measureSummary = this.measureRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(MeasureSummary.class,id));
    if (measureSummary.getUserId().equals(principalMap.get("userid"))) {
      return this.measureRepository.removeById(id);
    } else {
      return null;
    }
  }

  /**
   * Remove all measuresummaries for the currently logged in user
   * @return a list of the deleted data
   */
  @Deprecated
  @DeleteMapping("/all/delete")
  @ResponseBody
  public List<MeasureSummary> deleteMeasureSummary(Principal principal) {
    Map<String, String> principalMap = this.controllerUtil.getPrincipalAsMap(principal);
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    LinkedHashMap linkedHashMap = (LinkedHashMap) auth.getPrincipal();
    return this.measureRepository.removeAllByUserId(principalMap.get("userid"));
  }

}

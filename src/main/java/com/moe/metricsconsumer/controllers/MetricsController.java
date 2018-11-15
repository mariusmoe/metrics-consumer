package com.moe.metricsconsumer.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.moe.metricsconsumer.apiErrorHandling.EntityNotFoundException;
import com.moe.metricsconsumer.models.measureSummary.MeasureSummary;
import com.moe.metricsconsumer.models.rewards.Achievement;
import com.moe.metricsconsumer.models.rewards.UserAchievement;
import com.moe.metricsconsumer.repositories.MeasureRepository;
import com.moe.metricsconsumer.repositories.UserAchievementRepository;
import org.codehaus.jackson.JsonNode;
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
    List<UserAchievement> userAchievements = this.userAchievementRepository.findByUserRef(newMeasureSummary.getUserId());
    System.out.println(relevantAchievements);
    System.out.println(userAchievements);

    String userAchievementId = "";
    // Loop over -> add achieved achievements to list as a list of UserAchievement
    for (Achievement achievement : relevantAchievements){
      if (achievement.isCumulative()) {
        UserAchievement userAchievement = userAchievements.stream()
          .filter(object -> achievement.getId().equals(object.getAchievementRef()))
          .findAny()
          .orElse(null);
        if (userAchievement != null) {

        }
        // The achievement is cumulative
        // find existing cumulative userAchievement or create new
        userAchievementId = "";
//        UserAchievement userAchievement = new UserAchievement(newMeasureSummary.getUserId(), achievement.getId(),null , history, null);

      }
      if (achievement.getTaskIdRef().equals(measureSummary.getTaskId())) {
        // The achievement belong to this task
        // Create/overwrite userAchievement
        userAchievementId = hashedName;
      }
    };

    // Batch save the achieved achievements


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

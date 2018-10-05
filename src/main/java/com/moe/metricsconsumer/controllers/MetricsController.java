package com.moe.metricsconsumer.controllers;

import com.moe.metricsconsumer.apiErrorHandling.EntityNotFoundException;
import com.moe.metricsconsumer.models.measureSummary.MeasureSummary;
import com.moe.metricsconsumer.repositories.MeasureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

  @RequestMapping("/resource")
  @ResponseBody
  public Principal user(Principal principal) {
    return principal;
  }

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


  @GetMapping("/{taskId}")
  @ResponseBody
  public MeasureSummary getMeasureSummary(@NonNull @PathVariable("taskId") String taskId) throws EntityNotFoundException {
    return this.measureRepository.findFirstByUserIdAndTaskId("001", taskId);
  }

  // TODO: add all solution routes to its own controller
  @GetMapping("/solution/{taskId}")
  @ResponseBody
  public MeasureSummary getSolutionMeasureSummary(@NonNull @PathVariable("taskId") String taskId) throws EntityNotFoundException {
    Query query = new Query();
    query.addCriteria(Criteria.where("isSolutionManual").is(true));
    query.addCriteria(Criteria.where("taskId").is(taskId));

    return this.mongoTemplate.findOne(query, MeasureSummary.class);
  }



  @PostMapping("/")
  @ResponseBody
  public MeasureSummary newMeasureSummary(@Valid @RequestBody MeasureSummary newMeasureSummary){
    return measureRepository.save(newMeasureSummary);
  }

  @PostMapping("/solution")
  @ResponseBody
  public MeasureSummary newSolutionMeasureSummary(@Valid @RequestBody MeasureSummary newMeasureSummary){
    return measureRepository.save(newMeasureSummary);
  }

  @GetMapping("/mod")
  @ResponseBody
  public String getMessageOfTheDay(Principal principal) {
    return "The message of the day is boring for user: " + principal.getName();
  }



}

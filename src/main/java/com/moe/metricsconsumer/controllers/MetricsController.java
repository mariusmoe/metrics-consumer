package com.moe.metricsconsumer.controllers;

import com.moe.metricsconsumer.apiErrorHandling.EntityNotFoundException;
import com.moe.metricsconsumer.models.measureSummary.MeasureSummary;
import com.moe.metricsconsumer.repositories.MeasureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.*;

@RequestMapping("/api")
@Controller
public class MetricsController {

  @Autowired
  private MeasureRepository measureRepository;

  @RequestMapping("/resource")
  @ResponseBody
  public Principal user(Principal principal) {
    return principal;
  }

  @GetMapping("/")
  @ResponseBody
  public List<MeasureSummary> getAllMeasureSummaries() {
    return measureRepository.findAll();
  }

  @GetMapping("/{id}")
  @ResponseBody
  public MeasureSummary getMeasureSummary(@NonNull @PathVariable("id") String id) throws EntityNotFoundException {
    Optional<MeasureSummary> bookmark = this.measureRepository.findById(id);
    if (bookmark.isPresent()) {
      return bookmark.get();
    } else {
      throw new EntityNotFoundException(MeasureSummary.class, id);
    }
  }


  @PostMapping("/")
  @ResponseBody
  public MeasureSummary newMeasureSummary(@Valid @RequestBody MeasureSummary newMeasureSummary){
    return measureRepository.save(newMeasureSummary);
  }

  @GetMapping("/mod")
  @ResponseBody
  public String getMessageOfTheDay(Principal principal) {
    return "The message of the day is boring for user: " + principal.getName();
  }


}

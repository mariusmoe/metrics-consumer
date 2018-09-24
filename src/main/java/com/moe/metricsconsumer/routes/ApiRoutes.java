package com.moe.metricsconsumer.routes;

import com.moe.metricsconsumer.apiErrorHandling.EntityNotFoundException;
import com.moe.metricsconsumer.models.measureSummary.MeasureSummary;
import com.moe.metricsconsumer.repositories.MeasureRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RequestMapping("/api")
@Controller
public class ApiRoutes {

  @Autowired
  private MeasureRepository measureRepository;

  @GetMapping(value = "/", produces = "application/json")
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
      throw new EntityNotFoundException(MeasureSummary.class, id.toString());
    }
  }


  @PostMapping("/")
  @ResponseBody
  public MeasureSummary newMeasureSummary(@Valid @RequestBody MeasureSummary newMeasureSummary){
    return measureRepository.save(newMeasureSummary);
  }


}

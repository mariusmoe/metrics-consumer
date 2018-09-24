package com.moe.metricsconsumer.controllers;

import com.moe.metricsconsumer.models.MeasureSummary;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MeasureRepository extends MongoRepository<MeasureSummary, String> {

  public MeasureSummary findFirstById(String id);
  public MeasureSummary getFirstByTaskName(String taskName);
  public List<MeasureSummary> findByTaskName(String taskName);
}

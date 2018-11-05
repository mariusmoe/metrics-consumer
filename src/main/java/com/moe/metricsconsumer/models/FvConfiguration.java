package com.moe.metricsconsumer.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.HashMap;


@Data
public class FvConfiguration {

  @Id
  private String id;

  @Indexed
  private String taskId;

  private HashMap<String, String> expressionFeature;

  public FvConfiguration(String taskId, HashMap<String, String> expressionFeature) {
    this.taskId = taskId;
    this.expressionFeature = expressionFeature;
  }
}

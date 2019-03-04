package com.moe.metricsconsumer.models;

import lombok.Data;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.HashMap;


@Data
public class FvConfiguration {

  @Id
  private String id;

  @Indexed
  private String taskId;

  private Binary expressionFeature;

  private Binary dataFeature;

  public FvConfiguration() {};

  public FvConfiguration(String taskId, Binary expressionFeature, Binary dataFeature) {
    this.taskId = taskId;
    this.expressionFeature = expressionFeature;
    this.dataFeature = dataFeature;
  }
}

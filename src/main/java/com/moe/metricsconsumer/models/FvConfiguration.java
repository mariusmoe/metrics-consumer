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

  public FvConfiguration(String taskId, Binary expressionFeature) {
    this.taskId = taskId;
    this.expressionFeature = expressionFeature;
  }
}

package com.moe.metricsconsumer.models;

import lombok.Data;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document
public class ExerciseDocument {

  @Id
  @Field
  private String id;

  @Field
  private String measureSummaryRef;

  @Field
  private String docType;

  @Field
  private Binary file;
}

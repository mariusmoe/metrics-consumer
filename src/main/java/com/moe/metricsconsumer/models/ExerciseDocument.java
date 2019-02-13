package com.moe.metricsconsumer.models;

import lombok.Data;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * This data structure represents one of potentially many exercise files connected to a measureSummary
 */
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
  private String originalName;

  @Field
  private Binary file;

  public ExerciseDocument() {}

  public ExerciseDocument(String measureSummaryRef, String docType, String originalName, Binary file) {
    this.measureSummaryRef = measureSummaryRef;
    this.docType = docType;
    this.originalName = originalName;
    this.file = file;
  }
}

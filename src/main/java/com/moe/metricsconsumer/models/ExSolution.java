package com.moe.metricsconsumer.models;


import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;


@Data
public class ExSolution {

  @Indexed
  private String exClassName;

  private String exTitle;

  private Integer exNumber;

  private String exContent;

  public ExSolution () {}

  /**
   * Represent a solution
   * @param exClassName Simmilar to package name
   * @param exTitle  the file name without ".java"
   * @param exNumber NOT IN USE
   * @param exContent File content as a string
   */
  public ExSolution(String exClassName, String exTitle, Integer exNumber, String exContent) {
    this.exClassName = exClassName;
    this.exTitle = exTitle;
    this.exNumber = exNumber;
    this.exContent = exContent;
  }
}


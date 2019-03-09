package com.moe.metricsconsumer.models;


import lombok.Data;

import java.util.List;

@Data
public class ExSolution {

  private String exClassName;

  private String exTitle;

  private Integer exNumber;

  private List<String> exContent;

  public ExSolution () {}

  public ExSolution(String exClassName, String exTitle, Integer exNumber, List<String> exContent) {
    this.exClassName = exClassName;
    this.exTitle = exTitle;
    this.exNumber = exNumber;
    this.exContent = exContent;
  }
}


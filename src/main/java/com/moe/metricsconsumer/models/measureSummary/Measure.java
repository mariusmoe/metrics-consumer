package com.moe.metricsconsumer.models.measureSummary;

import java.util.List;

public class Measure {

  private String measureProvider;

  private List<SpecificMeasure> specificMeasures;

  @Override
  public String toString() {
    return "Measure{" +
      "measureProvider='" + measureProvider + '\'' +
      ", specificMeasures=" + specificMeasures +
      '}';
  }

  public String getMeasureProvider() {
    return measureProvider;
  }

  public void setMeasureProvider(String measureProvider) {
    this.measureProvider = measureProvider;
  }

  public List<SpecificMeasure> getSpecificMeasures() {
    return specificMeasures;
  }

  public void setSpecificMeasures(List<SpecificMeasure> specificMeasures) {
    this.specificMeasures = specificMeasures;
  }
  public Measure() {}

  public Measure(String measureProvider, List<SpecificMeasure> specificMeasures) {
    this.measureProvider = measureProvider;
    this.specificMeasures = specificMeasures;
  }
}

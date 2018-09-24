package com.moe.metricsconsumer.models.measureSummary;

public class SpecificMeasure {

  private String name;

  private float value;

  @Override
  public String toString() {
    return "SpecificMeasure{" +
      "name='" + name + '\'' +
      ", value=" + value +
      '}';
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public float getValue() {
    return value;
  }

  public void setValue(float value) {
    this.value = value;
  }

  public SpecificMeasure() {}

  public SpecificMeasure(String name, float value) {
    this.name = name;
    this.value = value;
  }
}

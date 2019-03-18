package com.moe.metricsconsumer.models;

import com.moe.metricsconsumer.models.measureSummary.MeasureSummary;
import lombok.Data;
import no.hal.learning.fv.FeatureList;

@Data
public class FvResponse {

  private MeasureSummary measureSummary;

  private FeatureList featureList;

  public FvResponse() {}

  public FvResponse(MeasureSummary measureSummary, FeatureList featureList) {
    this.measureSummary = measureSummary;
    this.featureList = featureList;
  }
}

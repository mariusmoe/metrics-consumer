package com.moe.metricsconsumer.models;

import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;

@Data
public class AnalyticsUser {

  @Indexed
  private String userId;

  private String analyticsId;


  public AnalyticsUser() {}

  public AnalyticsUser(String userId, String analyticsId) {
    this.userId = userId;
    this.analyticsId = analyticsId;
  }
}

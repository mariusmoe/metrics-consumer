package com.moe.metricsconsumer.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties
public class IMetricsProviderConfig {

  private List<String> metricsProviders = new ArrayList<>();

  public void setMetricsProviders(List<String> metricsProviders) {
    this.metricsProviders = metricsProviders;
  }

  public List<String> getMetricsProviders() {
    return this.metricsProviders;
  }

}

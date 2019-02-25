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

  private List<String> providers = new ArrayList<>();

  public void setProviders(List<String> providers) {
    this.providers = providers;
  }

  public List<String> getIMetricsProvidersList() {
    return this.providers ;
  }

}

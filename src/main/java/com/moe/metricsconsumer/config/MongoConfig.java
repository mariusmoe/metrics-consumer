package com.moe.metricsconsumer.config;


import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;


@Configuration
public class MongoConfig extends AbstractMongoConfiguration {

  @Value("${mom.mongo.address}")
  private String mongoAddress;

  @Value("${mom.mongo.database}")
  private String mongoDatabase;


  @Override
  protected String getDatabaseName() {
    return mongoDatabase;
  }


  @Override
  public MongoClient mongoClient() {
    return new MongoClient(mongoAddress);
  }
}

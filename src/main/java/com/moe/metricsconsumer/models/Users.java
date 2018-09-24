package com.moe.metricsconsumer.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

public class Users {

  @Id
  public ObjectId _id;

  public String username;

  public String password;


}

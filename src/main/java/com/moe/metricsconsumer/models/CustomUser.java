package com.moe.metricsconsumer.models;

import lombok.Data;

import java.util.List;

@Data
public class CustomUser {

  private List<String> userid_sec;

  private String userid;

  private String name;

  private String email;

  private String profilephoto;

  public CustomUser() {}

  public CustomUser(List<String> userid_sec, String userid, String name, String email, String profilephoto) {
    this.userid_sec = userid_sec;
    this.userid = userid;
    this.name = name;
    this.email = email;
    this.profilephoto = profilephoto;
  }
}

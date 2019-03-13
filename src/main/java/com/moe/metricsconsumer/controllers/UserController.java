package com.moe.metricsconsumer.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@RequestMapping("/api")
@Controller
public class UserController {

  @RequestMapping("/resource")
  @ResponseBody
  public Principal user(Principal principal) {
    // TODO: add tracker for google analytics
    return principal;
  }

  @GetMapping("/mod")
  @ResponseBody
  public String getMessageOfTheDay(Principal principal) {
    return "The message of the day is boring for user: " + principal.getName();
  }

  // TODO: Add route for delete all user interaction
  // TODO: Add route for download of all user data


}

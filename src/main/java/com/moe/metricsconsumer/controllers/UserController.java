package com.moe.metricsconsumer.controllers;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.moe.metricsconsumer.models.AnalyticsUser;
import com.moe.metricsconsumer.models.CustomUser;
import com.moe.metricsconsumer.repositories.AnalyticsUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.security.Principal;
import java.util.*;

@RequestMapping("/api")
@Controller
public class UserController {

  @Autowired
  private AnalyticsUserRepository analyticsUserRepository;

  ControllerUtil controllerUtil = new ControllerUtil();

  @RequestMapping("/resource")
  @ResponseBody
  public ResponseEntity<ObjectNode> user(Principal principal) {
    Map<String, String> map = this.controllerUtil.getPrincipalAsMap(principal);
    String userId = map.get("userid");
    AnalyticsUser analyticsUser;
    AnalyticsUser analyticsUserOpt = this.analyticsUserRepository.findByUserId(userId).orElse(null);
    if (analyticsUserOpt == null) {
      System.out.println("User was null - creating a new one");
      UUID uuid = UUID.randomUUID();
      AnalyticsUser analyticsUserToSave = new AnalyticsUser(userId, uuid.toString());
      analyticsUser = this.analyticsUserRepository.save(analyticsUserToSave);
    } else {
      analyticsUser = analyticsUserOpt;
    }
    Map<String, String> res = new HashMap<>();
    res.put("GKey", analyticsUser.getAnalyticsId());

    return  new ResponseEntity<>( controllerUtil.jsonResponse(2000, "Success - login succeeded", res),
      HttpStatus.OK);
  }



  @GetMapping("/mod")
  @ResponseBody
  public String getMessageOfTheDay(Principal principal) {
    return "The message of the day is boring for user: " + principal.getName();
  }

  // Add route for delete all user interaction
  // Add route for download of all user data


}

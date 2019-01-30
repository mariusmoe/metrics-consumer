package com.moe.metricsconsumer.models.rewards;


import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Holds data about a user
 */
@Data
public class UserAchievement {

  @Id
  private String id;

  private String userRef;

  private String achievementRef;

  private AchievementState achievementState;

  private Map<String, Integer> history;

  private LocalDateTime dateAchieved;

  public UserAchievement(String userRef, String achievementRef, AchievementState achievementState, LocalDateTime dateAchieved, Map<String, Integer> history) {
    this.userRef = userRef;
    this.achievementRef = achievementRef;
    this.achievementState = achievementState;
    this.history = history;
    this.dateAchieved = dateAchieved;
  }

}

package com.moe.metricsconsumer.models.rewards;


import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.HashMap;

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

  private HashMap<String, String> history;

  private LocalDateTime dateAchieved;

  public UserAchievement(String userRef, String achievementRef, AchievementState achievementState, HashMap<String, String> history, LocalDateTime dateAchieved) {
    this.userRef = userRef;
    this.achievementRef = achievementRef;
    this.achievementState = achievementState;
    this.history = history;
    this.dateAchieved = dateAchieved;
  }

  public UserAchievement(String userRef, String achievementRef, AchievementState achievementState, LocalDateTime dateAchieved) {
    this(userRef, achievementRef, achievementState,null, dateAchieved);
  }
}

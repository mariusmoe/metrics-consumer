package com.moe.metricsconsumer.models;

import lombok.Data;
import org.springframework.data.annotation.Id;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

enum AchievementState {
  HIDDEN, REVEALED, UNLOCKED
}


@Data
public class Achievement {

  @Id
  private String id;

  @NotNull
  private int threshold;


  private boolean isCummulative;

  private String taskIdRef;

  @NotNull
  private String expression;

  @NotNull
  private String name;

  @NotNull
  private String description;

  @NotNull
  private AchievementState state;


  @Size(min = 1, max = 100)
  private int progress;



  public Achievement(@NotNull int threshold,
                     boolean isCummulative,
                     String taskIdRef,
                     @NotNull String expression,
                     @NotNull String name,
                     @NotNull String description,
                     @NotNull AchievementState state,
                     @Size(min = 1, max = 100) int progress) {
    this.threshold = threshold;
    this.isCummulative = isCummulative;
    this.taskIdRef = taskIdRef;
    this.expression = expression;
    this.name = name;
    this.description = description;
    this.state = state;
    this.progress = progress;
  }

  public Achievement(@NotNull int threshold,
                     @NotNull String expression,
                     @NotNull String name,
                     @NotNull String description,
                     @NotNull AchievementState state
                     ) {
    this(threshold, false, "", expression, name, description, state,1);
  }


}

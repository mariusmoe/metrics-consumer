package com.moe.metricsconsumer.models;

import lombok.Data;
import org.springframework.data.annotation.Id;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * An achievement can have three states
 *
 * Hidden   - the user can not yet see what the achievement is for
 * Revealed - The user can see the achievement
 * Unlocked - The user has achieved the achievement
 */
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


  /**
   *
   * @param threshold       Value must supass this value to achieve the achievement
   * @param isCummulative   Boolean indicating if the achievement shoudld be rewarded for accumulated score
   * @param taskIdRef       Reference to task if any
   * @param expression      Tell which metrics shoudl be included - must use qualified name of metric
   * @param name            Name of the achievement
   * @param description     A describprion of the achievement
   * @param state           See ->
   * @see AchievementState
   * @param progress        Progress where 1 is low and 100 is done, min 1, max 100.
   */
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

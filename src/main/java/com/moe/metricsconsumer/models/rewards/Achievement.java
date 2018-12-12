package com.moe.metricsconsumer.models.rewards;

import lombok.Data;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import java.util.Map;


@Data
public class Achievement {

  @Id
  private String id;

  @NotNull
  private int threshold;


  private boolean isCumulative;

  private String taskIdRef;

  @NotNull
  private Binary expression;

  @NotNull
  private String name;

  @NotNull
  private String description;



  /**
   *
   * @param threshold       Value must supass this value to achieve the achievement
   * @param isCumulative   Boolean indicating if the achievement shoudld be rewarded for accumulated score
   * @param taskIdRef       Reference to task if any
   * @param expression      Tell which metrics shoudl be included - must use qualified name of metric
   * @param name            Name of the achievement
   * @param description     A describprion of the achievement
   */
  public Achievement(@NotNull int threshold,
                     boolean isCumulative,
                     String taskIdRef,
                     @NotNull Binary expression,
                     @NotNull String name,
                     @NotNull String description
                     ) {
    this.threshold = threshold;
    this.isCumulative = isCumulative;
    this.taskIdRef = taskIdRef;
    this.expression = expression;
    this.name = name;
    this.description = description;

  }


}

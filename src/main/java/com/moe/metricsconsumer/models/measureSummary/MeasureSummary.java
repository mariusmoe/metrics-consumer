package com.moe.metricsconsumer.models.measureSummary;

import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import java.util.List;


@Data
public class MeasureSummary {



  @Id
  private String id;

  private String userId;
  private boolean isSolutionManual;


  private String taskName;


  private String taskId;


  private List<Measure> measures;



  public MeasureSummary() {}

  public MeasureSummary(String userId, String taskName, String taskId, List<Measure> measures) {
    this(userId, false, taskName, taskId, measures);
  }

  public MeasureSummary(String userId, boolean isSolutionManual, String taskName, String taskId, List<Measure> measures) {
    this.userId = userId;
    this.isSolutionManual = isSolutionManual;
    this.taskName = taskName;
    this.taskId = taskId;
    this.measures = measures;
  }

  public String getTaskId() {
    return taskId;
  }
}


//  {
//    taskName: "Account-oppgave",
//    taskId: "stateandbehavior.Account",
//    measures: [
//      {
//        measureProvider: "org.metrics.cyclomatic",
//        measures: [
//        {
//          name: "cyclomaticComplexity":
//          value: 7
//        }
//          ]
//      },
//      {
//        measureProvider: "no.hal.javalang",
//        measures: [
//          {
//            name: "for"
//            value: 7
//         },
//           {
//            name: "foreach"
//            value: 4
//           },
//           {
//            name: "while":
//            value: 1
//           }
//  }

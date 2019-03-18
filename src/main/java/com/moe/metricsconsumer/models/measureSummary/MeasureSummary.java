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

  private List<String> includedClasses;



  public MeasureSummary() {}

  public MeasureSummary(String userId, String taskName, String taskId, List<Measure> measures, List<String> includedClasses) {
    this(userId, false, taskName, taskId, measures, includedClasses);
  }

  public MeasureSummary(String userId, boolean isSolutionManual, String taskName, String taskId, List<Measure> measures,
                        List<String> includedClasses) {
    this.userId = userId;
    this.isSolutionManual = isSolutionManual;
    this.taskName = taskName;
    this.taskId = taskId;
    this.measures = measures;
    this.includedClasses = includedClasses;
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

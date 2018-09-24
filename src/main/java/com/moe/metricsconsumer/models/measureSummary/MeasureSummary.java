package com.moe.metricsconsumer.models.measureSummary;

import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import java.util.List;



public class MeasureSummary {
  //  {
//    taskName: "Account-oppgave",
//    taskId: "stateandbehavior.Account",
//    measures: [
//      {
//        measureProvider: "org.metrics.cyclomatic",
//        measures: {
//          "cyclomaticComplexity": 7
//        }
//      },
//      {
//        measureProvider: "no.hal.javalang",
//        measures: {
//          "for": 7,
//            "foreach": 4,
//            "while": 1
//        }
//      }
//    ]
//  }

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

  @Id
  public String id;

  @NotNull
  public String taskName;

  @NotNull
  public String taskId;

  @NotNull
  private List<Measure> measures;

  public MeasureSummary() {}

  public MeasureSummary(String taskName, String taskId, List<Measure> measures) {
    this.taskName = taskName;
    this.taskId = taskId;
    this.measures = measures;
  }


  @Override
  public String toString() {
    return "MeasureSummary{" +
      "id='" + id + '\'' +
      ", taskName='" + taskName + '\'' +
      ", taskId='" + taskId + '\'' +
      ", measures=" + measures.toString() +
      '}';
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTaskName() {
    return taskName;
  }

  public void setTaskName(String taskName) {
    this.taskName = taskName;
  }

  public String getTaskId() {
    return taskId;
  }

  public void setTaskId(String taskId) {
    this.taskId = taskId;
  }

  public List<Measure> getMeasures() {
    return measures;
  }

  public void setMeasures(List<Measure> measures) {
    this.measures = measures;
  }
}

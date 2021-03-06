import {FeatureList} from "./feature-list";

export interface MeasureSummary {
  // TODO: describe the measure object here - should be doable :)
  taskName:string;
  taskId: string;
  id: string;
  measures: SpecificMeasure[];
  includedClasses: string[];
}

export interface SpecificMeasure {
  measureProvider: string;
  specificMeasures: Measure[];
}

export interface Measure {
  name: string;
  value: number;
}

export interface Summary {
  id: string;
  userId: string;
  taskName:string;
  taskId:string;
  measures?:string;
  solutionManual: boolean;
}

export interface FvResponse {
  measureSummary: MeasureSummary ;
  featureList: FeatureList;
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

import { Component, OnInit } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {MeasureService} from "../../_services/measure.service";
import {ActivatedRoute, Router} from "@angular/router";
import {forkJoin } from "rxjs";
import {MeasureSummary} from "../../_models/measure-summary";
import {FeatureList} from "../../_models/feature-list";
import {Exercise} from "../../_models/exercise";


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  loading = true;
  showInstructions = false;
  taskName = ''

  multi: any[];
  measureSummaryStudent: MeasureSummary;

  view: any[] = [500, 300];

  // options
  showXAxis = true;
  showYAxis = true;
  gradient = false;
  showLegend = true;
  showXAxisLabel = false;
  xAxisLabel = 'Country';
  showYAxisLabel = false;
  yAxisLabel = 'Population';



  exercises: Exercise[] = [
    {value: '1', viewValue: 'Øving 01: Objekter og klasser, til⁣stand og oppførsel'},
    {value: '2', viewValue: 'Øving 02: Innkapsling og vali⁣dering'},
    {value: '3', viewValue: 'Øving 03: Klasser og testing'},
    {value: '4', viewValue: 'Øving 04: Objektstrukturer med app'},
    {value: '5', viewValue: 'Øving 05: Objektstrukturer'},
    {value: '6', viewValue: 'Øving 06: Grensesnitt'},
    {value: '7', viewValue: 'Øving 07: Filbehandling med app'},
    {value: '8', viewValue: 'Øving 08: Observatør-Observert og Delegering'},
    {value: '9', viewValue: 'Øving 09: Arv og abstrakte klasser'},
  ]

  xAxisTickFormatting = function trimLabel(s, max = 40): string {
  return s;//.split(":", 2)[1];
  };


  colorScheme = {
    "name":"solar",
    "selectable":true,
    "group":"Continuous",
    "domain":["#fff8e1","#ffecb3","#ffe082","#ffd54f","#ffca28","#ffc107","#ffb300","#ffa000",
              "#ff8f00","#ff6f00"]
  };
  studentList: FeatureList;
  solutionManualList: FeatureList;
  objectKeys = Object.keys;

  constructor(
    private http: HttpClient,
    private measureService: MeasureService,
    private route: ActivatedRoute,
    private router: Router,
  ) { }

  ngOnInit() {

    // get taskId from param in url. If no param -> show some instructions
    this.route.paramMap.subscribe( params => {
      // this.retriveData(params.get('taskId'))
      if (params.get('taskId') != null) {
        this.retrieveFvData(params.get('taskId'))
        this.showInstructions = false;
      } else {
        this.showInstructions = true;
      }
    });

  }

  /**
   * Retreive unconfigured measure data
   * @param taskId task id 
   */
  retriveData(taskId: string) {

    let student = this.measureService.getMeasureData(taskId);
    let solutionManual = this.measureService.getSolutionMeasureData(taskId);

    forkJoin([student, solutionManual]).subscribe(results => {
      if (results[0]) {
        // results[0] is student
        // results[1] is solutionManual
        console.log(results);
        this.taskName = taskId;
        let studentData = [];
        this.measureSummaryStudent = results[0];
        let count = 0;

        // Go through all measures and add them to the heatmap
        // Important -> Assumes that solution guide and student has the same number of measures
        results[0].measures.forEach((measure, measureIndex) => {
          measure.specificMeasures.forEach((specificMeasure, index) => {
            let localMeasureID = count//(`0${measureIndex}`).slice(-2) + (`0${index}`).slice(-2);
            count++;
            studentData.push({
              name: localMeasureID + ':' + measure.specificMeasures[index].name,
              series: [
                {name: "Student", value: results[0].measures[measureIndex].specificMeasures[index].value},
                {name: "Solution", value: results[1].measures[measureIndex].specificMeasures[index].value || 0}
              ]
            })
          })
        });

        console.log( studentData);
        this.multi = [...studentData];
        this.loading = false;
      }

    });
  }

  /**
   * Retreive the Fv for the specified task
   * @param taskId 
   */
  retrieveFvData (taskId: string) {
    let student = this.measureService.getMeasureFvData(taskId);
    let solutionManual = this.measureService.getSolutionFvMeasureData(taskId);
    console.log('FV');

    forkJoin([student, solutionManual]).subscribe(results => {
      if (results[0]) {
        this.studentList = results[0]; // is student
        this.solutionManualList = results[1]; // is solutionManual
        console.log(results);
        this.taskName = this.exercises.find(obj => obj.value === taskId).viewValue;
        let studentData = [];

        // Go through all measures and add them to the heatmap
        // Important -> Assumes that solution guide and student has the same number of measures
        results[0].featureNames.forEach((featureName, index) => {
          let student = results[0].features.find(o => Object.keys(o)[0] == featureName);
          let Solution = results[1].features.find(o => Object.keys(o)[0] == featureName);

          studentData.push({
            name: featureName,
            series: [
              { name: "Student", value: student[Object.keys(student)[0]] },
              // { name: "Solution", value: studentValue || 0 }
              // Alternatively: does not support value based filtering!
              { name: "Solution", value: Solution[Object.keys(Solution)[0]] }
             ]
          })
        })

        console.log( studentData);
        this.loading = false;
        this.multi = [...studentData];
      }

    });
  }

  onSelect(event) {
    console.log(event);
    console.log(event.series.split(":", 2)[0]);
  }


  findValueInSolutionManualList(val) {

    let solutionObject = this.solutionManualList.features.find( obj => {
      return Object.keys(obj)[0] === val});
    if (solutionObject == null) {
      return undefined
    } else {

      return solutionObject[Object.keys(solutionObject)[0]];
    }
  }

  public getFeatureTitle(title) {
    if (title[0].includes(":")) {
      return title[0].substring(0,title[0].indexOf(":"));

    }
    return title;
  }

  public getFeatureBody(title: string) {
    if (title[0].includes(":")) {
      return title[0].substring(title[0].indexOf(":")+1);
    }
    return title;
  }



}

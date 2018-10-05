import { Component, OnInit } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {MeasureService} from "../../_services/measure.service";
import {MeasureSummary} from "../../_models/measure-summary";
import {multi} from '../../_models/data';
import {switchMap} from "rxjs/operators";
import {ActivatedRoute, ParamMap, Router} from "@angular/router";
import {forkJoin, Observable} from "rxjs";


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  loading = true;
  measureSummary$;
  error;
  _object = Object;


  single: any[];
  multi: any[];

  view: any[] = [500, 280];

  // options
  showXAxis = true;
  showYAxis = true;
  gradient = false;
  showLegend = true;
  showXAxisLabel = false;
  xAxisLabel = 'Country';
  showYAxisLabel = false;
  yAxisLabel = 'Population';

  yAxisTickFormatting = function trimLabel(s, max = 40): string {
    if(typeof s !== 'string') {
      if(typeof s === 'number') {
        return s + '';
      } else {
        return '';
      }
    }

    s = s.trim();
    if(s.length <= max) {
      return s;
    } else {
      return `${s.slice(0, max)}...`;
    }
  }
  // yAxisTicks = ['','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','',];

  colorScheme = {
    "name":"solar",
    "selectable":true,
    "group":"Continuous",
    "domain":["#fff8e1","#ffecb3","#ffe082","#ffd54f","#ffca28","#ffc107","#ffb300","#ffa000",
              "#ff8f00","#ff6f00"]
  };



  masterGridHeadders = ['Measure', 'your app', 'solution guide']
  private solutionMeasureSummary$: Observable<any>;
  private paramObservable$: Observable<any>;



  constructor(
    private http: HttpClient,
    private measureService: MeasureService,
    private route: ActivatedRoute,
    private router: Router,
  ) {
    // TODO - create simple datastructure?
    // ANd based on event click (name) find and display further data ?
    Object.assign(this, {multi})


  }

  ngOnInit() {


    // this.route.paramMap.pipe(
    //   switchMap((params: ParamMap) =>
    //     this.retriveData(params.get('taskId')))
    // );
    //
    // this.paramObservable$.subscribe(data)

    this.route.paramMap.subscribe( params =>
      this.retriveData(params.get('taskId'))
      
    );

    // this.measureSummary$.subscribe( data => {
    //   this.handleMeasureSummary(data);
    // })
    // https://coryrylan.com/blog/angular-multiple-http-requests-with-rxjs
  }

  retriveData(taskId: string) {

    let student = this.measureService.getMeasureData(taskId);
    let solutionManual = this.measureService.getSolutionMeasureData(taskId);

    forkJoin([student, solutionManual]).subscribe(results => {
      // results[0] is student
      // results[1] is solutionManual
      console.log(results);
      let studentData = [];
      // Horizontal data display (long names on yaxis are cut)
      // results.forEach((data, resultsIndex) => {
      //   data.measures.forEach(measure=> {
      //     measure.specificMeasures.forEach((specificMeasure, index) => {
      //       studentData[resultsIndex].series.push({
      //         name: measure.specificMeasures[index].name,
      //         value: measure.specificMeasures[index].value
      //       })
      //     });
      //   });
      //
      // })
      results.forEach((data, resultsIndex) => {
        data.measures.forEach(measure => {
          measure.specificMeasures.forEach((specificMeasure, index) => {
            studentData.push({
              name: measure.specificMeasures[index].name,
              series: [
                {name: "Student", value: measure.specificMeasures[index].value},
                {name: "Solution", value: measure.specificMeasures[index].value}
              ]
            })
          })
        })
      });
      console.log( studentData)
      this.multi = [...studentData];
      this.loading = false;
    });
  }

  onSelect(event) {
    console.log(event);
  }

  // handleMeasureSummary(data: MeasureSummary) {
  //     console.log(data);
  //     let studentData = [{
  //       name: "Student",
  //       series: []
  //     }]
  //     data.measures.forEach(measure=> {
  //       console.log(measure);
  //       measure.specificMeasures.forEach((specificMeasure, index) => {
  //         studentData[0].series.push({
  //           name: measure.specificMeasures[index].name,
  //           value: measure.specificMeasures[index].value
  //         })
  //       });
  //     });
  //     console.log( studentData)
  //     this.multi = [...studentData];
  //     this.data = {...data}
  //
  // }






}

import { Component, OnInit } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {MeasureService} from "../../_services/measure.service";
import {MeasureSummary} from "../../_models/measure-summary";
import {multi} from '../../_models/data';
import {switchMap} from "rxjs/operators";
import {ActivatedRoute, ParamMap, Router} from "@angular/router";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  data;
  measureSummary$;
  error;
  _object = Object;
  studentData = [{
    name: "Student",
    series: []
  }]

  single: any[];
  multi: any[];

  view: any[] = [700, 400];

  // options
  showXAxis = true;
  showYAxis = true;
  gradient = false;
  showLegend = true;
  showXAxisLabel = true;
  xAxisLabel = 'Country';
  showYAxisLabel = true;
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
    this.measureSummary$ = this.route.paramMap.pipe(
      switchMap((params: ParamMap) =>
        this.measureService.getMeasureData(params.get('taskId')))
    );
  }

  onSelect(event) {
    console.log(event);
  }

  getdata(taskId: string) {
    this.measureService.getMeasureData(taskId).subscribe(
      (data: MeasureSummary[]) => {
        console.log(data);
        data.forEach(measureSummary => {
          console.log(measureSummary.id);

          this.studentData[0].series.push({
            name: measureSummary.measures[0].specificMeasures[0].name,
            value: measureSummary.measures[0].specificMeasures[0].value
          })
        });
        console.log( this.studentData)
        this.multi = this.studentData;
        this.data = {...data}
        },
          error => {this.error = error;}
    );
  }






}

import { Component, OnInit } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {MeasureService} from "../../_services/measure.service";
import {ActivatedRoute, Router} from "@angular/router";
import {forkJoin } from "rxjs";


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  loading = true;
  taskName = ''

  multi: any[];

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

  xAxisTickFormatting = function trimLabel(s, max = 40): string {
  return s.split(":", 2)[1];
  };


  colorScheme = {
    "name":"solar",
    "selectable":true,
    "group":"Continuous",
    "domain":["#fff8e1","#ffecb3","#ffe082","#ffd54f","#ffca28","#ffc107","#ffb300","#ffa000",
              "#ff8f00","#ff6f00"]
  };

  constructor(
    private http: HttpClient,
    private measureService: MeasureService,
    private route: ActivatedRoute,
    private router: Router,
  ) { }

  ngOnInit() {
    this.route.paramMap.subscribe( params =>
      this.retriveData(params.get('taskId'))
    );

  }

  retriveData(taskId: string) {

    let student = this.measureService.getMeasureData(taskId);
    let solutionManual = this.measureService.getSolutionMeasureData(taskId);

    forkJoin([student, solutionManual]).subscribe(results => {
      // results[0] is student
      // results[1] is solutionManual
      console.log(results);
      this.taskName = taskId;
      let studentData = [];

        // Go through all measures and add them to the heatmap
        // Important -> Assumes that solution guide and student has the same number of measures
        results[0].measures.forEach((measure, measureIndex) => {
          measure.specificMeasures.forEach((specificMeasure, index) => {
            let localMeasureID = (`0${measureIndex}`).slice(-2) + (`0${index}`).slice(-2);
            studentData.push({
              name: localMeasureID + ':' + measure.specificMeasures[index].name,
              series: [
                {name: "Student", value: results[0].measures[measureIndex].specificMeasures[index].value},
                {name: "Solution", value: results[1].measures[measureIndex].specificMeasures[index].value}
              ]
            })
          })
        });

      console.log( studentData);
      this.multi = [...studentData];
      this.loading = false;
    });
  }

  onSelect(event) {
    console.log(event);
    console.log(event.series.split(":", 2)[0]);
  }





}

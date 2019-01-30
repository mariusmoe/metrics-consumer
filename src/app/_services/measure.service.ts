import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";

import {Observable, throwError} from 'rxjs';
import {Measure, MeasureSummary, Summary} from "../_models/measure-summary";


import {catchError} from "rxjs/operators";
import { FeatureList } from '../_models/feature-list';
import {ErrorService} from "./error.service";

@Injectable({
  providedIn: 'root'
})
export class MeasureService {

  constructor(
    private http: HttpClient,
    private errorService: ErrorService)
  { }

  getMeasureData(taskId: string) {
    console.log("get measure service called");
    return this.http.get<MeasureSummary>(`api/${taskId}`)
      .pipe(
        catchError(this.errorService.handleError)
      );
  }

  getSolutionMeasureData(taskId: string) {
    console.log("get solution service called");
    return this.http.get<MeasureSummary>(`api/solution/${taskId}`)
      .pipe(
        catchError(this.errorService.handleError)
      );
  }

  getMeasureFvData(taskId: string) {
    console.log("get measure service called");
    return this.http.get<FeatureList>(`api/fv/${taskId}`)
      .pipe(
        catchError(this.errorService.handleError)
      );
  }

  getSolutionFvMeasureData(taskId: string) {
    console.log("get solution service called");
    return this.http.get<FeatureList>(`api/fv/solution/${taskId}`)
      .pipe(
        catchError(this.errorService.handleError)
      );
  }

  getSummaries() {
    console.log("get summaries called");
    return this.http.get<Summary[]>('api/')
      .pipe(
        catchError(this.errorService.handleError)
      );
  }

  deleteAllUserdata() {
    return this.http.delete<MeasureSummary[]>("api/all/delete")
      .pipe(
        catchError(this.errorService.handleError)
      );
  }





}

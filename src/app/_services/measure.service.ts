import {EventEmitter, Injectable, Output} from '@angular/core';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";

import {Observable, throwError} from 'rxjs';
import {FvResponse, Measure, MeasureSummary, Summary} from "../_models/measure-summary";


import {catchError, tap} from "rxjs/operators";
import { FeatureList } from '../_models/feature-list';
import {ErrorService} from "./error.service";

@Injectable({
  providedIn: 'root'
})
export class MeasureService {

  @Output() newFiles: EventEmitter<boolean> = new EventEmitter();

  constructor(
    private http: HttpClient,
    private errorService: ErrorService)
  { }

  getMeasureData(taskId: string) {
    return this.http.get<MeasureSummary>(`api/${taskId}`)
      .pipe(
        tap(_ => console.log("getMeasureData service called")),
        catchError(this.errorService.handleError)
      );
  }

  getSolutionMeasureData(taskId: string) {
    return this.http.get<MeasureSummary>(`api/solution/${taskId}`)
      .pipe(
        tap(_ => console.log("getSolutionMeasureData service called")),
        catchError(this.errorService.handleError)
      );
  }

  getMeasureFvData(taskId: string) {
    return this.http.get<FvResponse>(`api/fv/${taskId}`)
      .pipe(
        tap(_ => console.log("getMeasureFvData service called")),
        catchError(this.errorService.handleError)
      );
  }

  getSolutionFvMeasureData(taskId: string) {
    return this.http.get<FvResponse>(`api/fv/solution/${taskId}`)
      .pipe(
        tap(_ => console.log("getSolutionFvMeasureData service called")),
        catchError(this.errorService.handleError)
      );
  }

  getSummaries() {
    return this.http.get<Summary[]>('api/')
      .pipe(
        tap(_ => console.log('get summaries called')),
        catchError(this.errorService.handleError)
      );
  }

  deleteAllUserdata() {
    return this.http.delete<MeasureSummary[]>("api/all/delete")
      .pipe(
        tap(_ => console.log('deleteAllUserdata called')),
        catchError(this.errorService.handleError)
      );
  }


  notifyNewFiles() {
      this.newFiles.emit();
  }




}

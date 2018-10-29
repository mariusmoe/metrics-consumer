import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";

import {Observable, throwError} from 'rxjs';
import {Measure, MeasureSummary, Summary} from "../_models/measure-summary";


import {catchError} from "rxjs/operators";
import { FeatureList } from '../_models/feature-list';


@Injectable({
  providedIn: 'root'
})
export class MeasureService {

  constructor(private http: HttpClient) { }

  getMeasureData(taskId: string) {
    console.log("get measure service called");
    return this.http.get<MeasureSummary>(`api/${taskId}`)
      .pipe(
        catchError(this.handleError)
      );
  }

  getSolutionMeasureData(taskId: string) {
    console.log("get solution service called");
    return this.http.get<MeasureSummary>(`api/solution/${taskId}`)
      .pipe(
        catchError(this.handleError)
      );
  }

  getMeasureFvData(taskId: string) {
    console.log("get measure service called");
    return this.http.get<FeatureList>(`api/fv/${taskId}`)
      .pipe(
        catchError(this.handleError)
      );
  }

  getSolutionFvMeasureData(taskId: string) {
    console.log("get solution service called");
    return this.http.get<FeatureList>(`api/fv/${taskId}`)
      .pipe(
        catchError(this.handleError)
      );
  }

  getSummaries() {
    console.log("get summaries called");
    return this.http.get<Summary[]>('api/')
      .pipe(
        catchError(this.handleError)
      );
  }

  deleteAllUserdata() {
    return this.http.delete<MeasureSummary[]>("api/all/delete")
      .pipe(
        catchError(this.handleError)
      );
  }




  private handleError(error: HttpErrorResponse) {
    if (error.error instanceof ErrorEvent) {
      // A client-side or network error occurred. Handle it accordingly.
      console.error('An error occurred:', error.error.message);
    } else {
      // The backend returned an unsuccessful response code.
      // The response body may contain clues as to what went wrong,
      console.error(
        `Backend returned code ${error.status}, ` +
        `body was: ${error.error}`);
    }
    // return an observable with a user-facing error message
    return throwError(
      'Something bad happened; please try again later.');
  };
}

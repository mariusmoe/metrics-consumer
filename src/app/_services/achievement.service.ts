import { Injectable } from '@angular/core';

import {catchError} from "rxjs/operators";
import {ErrorService} from "./error.service";
import {HttpClient} from "@angular/common/http";
import {Achievement} from "../_models/achievement";

@Injectable({
  providedIn: 'root'
})
export class AchievementService {

  constructor(
    private http: HttpClient,
    private errorService: ErrorService)
  { }

  getAllAchievementsFromService() {
    console.log("get All Achievements called");
    return this.http.get<Achievement[]>(`api/achievement/`)
      .pipe(
        catchError(this.errorService.handleError)
      );
  }


}

import { Injectable } from '@angular/core';

import {catchError} from "rxjs/operators";
import {ErrorService} from "./error.service";
import {HttpClient} from "@angular/common/http";
import {Achievement} from "../_models/achievement";
import {UserAchievement} from "../_models/user-achievement";
import {forkJoin, Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AchievementService {

  constructor(
    private http: HttpClient,
    private errorService: ErrorService)
  { }

  /**
   * Get a list of all achievements without binary data
   */
  getAllAchievementsFromService() {
    console.log("get All Achievements called");
    return this.http.get<Achievement[]>(`api/achievement/`)
      .pipe(
        catchError(this.errorService.handleError)
      );
  }

  /**
   * Get a list of all the achieved achievements for the logged in user
   */
  getAllUserAchievementsFromService() {
    console.log("get All Achievements called");
    return this.http.get<UserAchievement[]>(`api/achievement/user`)
      .pipe(
        catchError(this.errorService.handleError)
      );
  }

  /**
   * Wait for all achievement data before returning
   */
  getAllAchievementDataFromService(): Observable<any[]> {
    let res1 = this.getAllAchievementsFromService();
    let res2 = this.getAllUserAchievementsFromService();
    return forkJoin([res1, res2]);
  }
}

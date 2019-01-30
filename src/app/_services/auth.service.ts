import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import {HttpClient} from "@angular/common/http";
import {catchError, map} from 'rxjs/operators';
import {Summary} from "../_models/measure-summary";
import {ErrorService} from "./error.service";


@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private _isLoggedIn = false;

  constructor(
    private http: HttpClient,
    private errorService: ErrorService)
  { }

  get isLoggedIn(): boolean {
    return this._isLoggedIn;
  }


  getResource(){
    console.log("get summaries called");
    this.http.get('api/resource')
      .pipe(
        catchError(this.errorService.handleError)
      ).subscribe(data => {
        console.log(data);
        this._isLoggedIn = true;
        localStorage.setItem('currentUser', JSON.stringify(data));
        // (<any>window).ga('set', 'userId', data['somefield']); // Set the user ID using signed-in user_id.
      }, error => {
        try {
          localStorage.removeItem('currentUser');
        } catch (e) {
          console.error(e);
        }
        console.log('Not logged in', error)

      });



  }


  logout(): Observable<boolean> {
    this._isLoggedIn = false;

    try {
      localStorage.removeItem('currentUser');
    } catch (e) {
      console.error(e);
    }

    return this.http.post('logout', {}).pipe(map(() => true))
  }

}

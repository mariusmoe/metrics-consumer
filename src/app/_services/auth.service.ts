import {EventEmitter, Injectable, Output} from '@angular/core';
import { Observable, of } from 'rxjs';
import {HttpClient} from "@angular/common/http";
import {catchError, map} from 'rxjs/operators';
import {Summary} from "../_models/measure-summary";
import {ErrorService} from "./error.service";


@Injectable({
  providedIn: 'root'
})
export class AuthService {


  @Output() isLoggedIn: EventEmitter<boolean> = new EventEmitter();

  constructor(
    private http: HttpClient,
    private errorService: ErrorService)
  { }

  getResource(){
    console.log("get summaries called");
    return this.http.get('api/resource')
      .pipe(
        catchError(this.errorService.handleError)
      );
  }


  logout() {
    this.isLoggedIn.emit(false);
    try {
      localStorage.removeItem('currentUser');
    } catch (e) {
      console.error(e);
    }
// https://auth.dataporten.no/logout

  }


  setIsLoggedIn(isLoggedIn:boolean) {
    this.isLoggedIn.emit(isLoggedIn);
  }

}

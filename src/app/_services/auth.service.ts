import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import {HttpClient} from "@angular/common/http";
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private _isLoggedIn = false;

  constructor(private http: HttpClient) { }

  get isLoggedIn(): boolean {
    return this._isLoggedIn;
  }


  getResource() {
    this.http.get('api/resource').subscribe(data => {
      console.log(data);
      this._isLoggedIn = true;
      localStorage.setItem('currentUser', JSON.stringify(data));
    }, error => {
      try {
        localStorage.removeItem('currentUser');
      } catch (e) {
        console.error(e);
      }
      console.log('Not logged in')
    });
  }


  logout(): Observable<boolean> {
    try {
      localStorage.removeItem('currentUser');
    } catch (e) {
      console.error(e);
    }

     return this.http.post('logout', {}).pipe(map(() => true))

  }

}

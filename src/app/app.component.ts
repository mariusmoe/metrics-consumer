import { Component, ViewChild } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {MatSidenav} from '@angular/material/sidenav';
import {Router, RouterOutlet} from "@angular/router";


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Demo';
  data:any;

  isLoggedIn: boolean;


  constructor(private http: HttpClient, private router: Router) {
    http.get('api/resource').subscribe(data => {
      console.log(data);
      this.data = data
      this.isLoggedIn = true;
    });
  }


  login() {
    window.location.href = "/login";
  }

  logout() {
    this.http.post('logout', {}).subscribe(
      res => {
        console.log(res)
        this.router.navigateByUrl('/login');
      }, error => {
        // We do not need to parse the response. As long as the session is destroyed we are happy.
        console.log(error)
        this.router.navigateByUrl('/login');
      })
  }


}

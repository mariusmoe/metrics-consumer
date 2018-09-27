import { Component, OnInit } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  data;

  ngOnInit() {
  }

  constructor(private http: HttpClient, private router: Router) {
  }

  getdata() {
    this.http.get('api/').subscribe(data => {
      console.log(data);
      this.data = data
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

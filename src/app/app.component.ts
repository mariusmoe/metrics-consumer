import { Component, ViewChild } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {MatSidenav} from '@angular/material/sidenav';
import {NavigationEnd, Router, RouterOutlet} from "@angular/router";
import {MeasureSummary, Summary} from "./_models/measure-summary";
import {MeasureService} from "./_services/measure.service";
import {Observable} from "rxjs";
import {AuthService} from "./_services/auth.service";


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Demo';
  data:any;

  isLoggedIn: boolean;

  summaries: Summary[];


  constructor(
    private http: HttpClient,
    private router: Router,
    private measureService: MeasureService,
    private authService: AuthService
  ) {
    this.authService.getResource();

    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        (<any>window).ga('set', 'page', event.urlAfterRedirects);
        (<any>window).ga('send', 'pageview');
      }
    });

    this.measureService.getSummaries().subscribe(
      (data: Summary[]) => {
        console.log(data);
        this.summaries = data;
      });
  }



  login() {
    window.location.href = "/login";
  }

  logout() {
    this.authService.logout().subscribe(result => {
      if (result === true) {
        this.router.navigateByUrl('/login');
      } else {
        console.error('Could not logout at this moment');
      }
    })
  }


}

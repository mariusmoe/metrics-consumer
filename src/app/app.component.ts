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
    this.authService.getResource().subscribe(data => {
      console.log(data);
      this.authService.setIsLoggedIn(true);
      localStorage.setItem('currentUser', JSON.stringify(data));

      // (<any>window).ga('set', 'userId', data['somefield']); // Set the user ID using signed-in user_id.

      this.loadSummaries();

    }, error => {
      try {
        localStorage.removeItem('currentUser');
      } catch (e) {
        console.error(e);
      }
      console.log('Not logged in', error)

    });

    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        (<any>window).ga('set', 'page', event.urlAfterRedirects);
        (<any>window).ga('send', 'pageview');
      }
    });


  }

  ngOnInit() {
    this.measureService.newFiles.subscribe(o => {
      this.loadSummaries();
    });
    this.authService.isLoggedIn.subscribe(o => {
      this.isLoggedIn = o;
    });
  }



  login() {
    window.location.href = "/login";
  }

  logout() {
    this.authService.logout();
    this.measureService.notifyNewFiles();
    window.location.href = "/logout"
  }

  loadSummaries(){
    this.measureService.getSummaries().subscribe(
      (data: Summary[]) => {
        console.log(data);
        this.summaries = data;
      }, error => {
        this.summaries = null;
      });
  }


}

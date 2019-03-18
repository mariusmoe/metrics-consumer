import { Component, ViewChild } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {MatSidenav} from '@angular/material/sidenav';
import {NavigationEnd, Router, RouterOutlet} from "@angular/router";
import {MeasureSummary, Summary} from "./_models/measure-summary";
import {MeasureService} from "./_services/measure.service";
import {Observable} from "rxjs";
import {AuthService} from "./_services/auth.service";
import {Exercise} from "./_models/exercise";

export interface PrincipalResource {
  message: string;
  status: number;
  details?: object;
}

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

  exercises: Exercise[] = [
    {value: '1', viewValue: 'Øving 01: Objekter og klasser, til⁣stand og oppførsel'},
    {value: '2', viewValue: 'Øving 02: Innkapsling og vali⁣dering'},
    {value: '3', viewValue: 'Øving 03: Klasser og testing'},
    {value: '4', viewValue: 'Øving 04: Objektstrukturer med app'},
    {value: '5', viewValue: 'Øving 05: Objektstrukturer'},
    {value: '6', viewValue: 'Øving 06: Grensesnitt'},
    {value: '7', viewValue: 'Øving 07: Filbehandling med app'},
    {value: '8', viewValue: 'Øving 08: Observatør-Observert og Delegering'},
    {value: '9', viewValue: 'Øving 09: Arv og abstrakte klasser'},
  ]

  constructor(
    private http: HttpClient,
    private router: Router,
    private measureService: MeasureService,
    private authService: AuthService
  ) {
    this.authService.getResource().subscribe((data: PrincipalResource)  => {
      // console.log(data);
      this.authService.setIsLoggedIn(true);
      localStorage.setItem('currentUser', JSON.stringify(data.details[0]));
      // console.log(data.details[0][Object.keys(data.details[0])[0]]);
      let gKey = data.details[0][Object.keys(data.details[0])[0]];

      (<any>window).ga('set', 'userId', gKey);

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
        // console.log(data);
        this.summaries = data;
      }, error => {
        this.summaries = null;
      });
  }

  notUploadedSummaries(summaries: Summary[]): Exercise[] {
    if (summaries) {
      let ex = this.exercises;
      return ex.filter( (o) => {
        if (!summaries.find(s => s.taskId === o.value)) {
          return o
        }
      })
    }
  }


}

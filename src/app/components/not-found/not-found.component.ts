import { Component, OnInit } from '@angular/core';
import {Summary} from "../../_models/measure-summary";
import {AuthService} from "../../_services/auth.service";

@Component({
  selector: 'app-not-found',
  templateUrl: './not-found.component.html',
  styleUrls: ['./not-found.component.css']
})
export class NotFoundComponent implements OnInit {

  loggedIn = false;

  constructor(private authService: AuthService) { }

  ngOnInit() {}
  // ngOnInit() {
  //   this.authService.isLoggedIn().subscribe(
  //     (res: boolean) => {
  //       console.log(res);
  //       this.loggedIn = res;
  //     });
  // }





}

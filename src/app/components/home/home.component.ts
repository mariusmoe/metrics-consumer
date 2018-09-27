import { Component, OnInit } from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  data;

  ngOnInit() {
  }

  constructor(private http: HttpClient) {
  }

  getdata() {
    this.http.get('api/').subscribe(data => {
      console.log(data);
      this.data = data
    });
  }




}

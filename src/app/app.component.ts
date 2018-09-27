import { Component } from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Demo';
  data:any;
  constructor(private http: HttpClient) {
    http.get('api/resource').subscribe(data => {
      console.log(data);
      this.data = data});
  }


}

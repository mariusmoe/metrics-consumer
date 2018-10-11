import { Component, OnInit } from '@angular/core';
import {MeasureService} from "../../_services/measure.service";

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit {

  constructor( private measureService: MeasureService) { }

  ngOnInit() {
  }

  deleteUserdata() {
    this.measureService.deleteAllUserdata().subscribe(res => {
      console.log(res);
    })
  }

}

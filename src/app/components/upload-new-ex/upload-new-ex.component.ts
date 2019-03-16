import { Component, OnInit } from '@angular/core';
import {FileUploaderCustom} from "../../_services/FileUploaderCustom";
import {FileItem, ParsedResponseHeaders} from 'ng2-file-upload';
import {Exercise} from "../../_models/exercise";
import {MeasureService} from "../../_services/measure.service";
import {environment} from "../../../environments/environment";
import {MatSnackBar} from "@angular/material";
import {switchMap} from "rxjs/operators";
import {ActivatedRoute, ParamMap, Router} from "@angular/router";


const URL = 'http://localhost:8080/api/xml/';



@Component({
  selector: 'app-upload-new-ex',
  templateUrl: './upload-new-ex.component.html',
  styleUrls: ['./upload-new-ex.component.css']
})
export class UploadNewExComponent implements OnInit {

  public uploader:FileUploaderCustom ;

  public canSubmit = false;
  public exerciseToUpload = '1';
  private sub: any;

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
    private measureService: MeasureService,
    private snackBar: MatSnackBar,
    private route: ActivatedRoute,
    private router: Router,
  ) {
    this.uploader = new FileUploaderCustom({
      url: environment.uploadUrl,
      itemAlias: "uploadingFiles",
      headers: [{
        name: "exNumber",
        value: "3"
      }]
    });
    this.uploader.onErrorItem   = (item, response, status, headers) => this.onErrorItem(item, response, status, headers);
    this.uploader.onSuccessItem = (item, response, status, headers) => this.onSuccessItem(item, response, status, headers);
  }

  ngOnInit() {
    this.sub = this.route.params.subscribe(params => {
      this.exerciseToUpload = params['id'];
      console.log("param changed " + params['id'])

      this.uploader.options.headers.map((obj) => {
        if (obj.name == "exNumber") {
          obj.value = params['id'];
        }
      });
      console.log(this.uploader.options.headers);
      this.canSubmit = true;
    });
  }


  onSuccessItem(item: FileItem, response: string, status: number, headers: ParsedResponseHeaders): any {
    this.openSnackBar("Opplasting vellykket", "OK");
    let data = JSON.parse(response); //success server response
    console.log("File upload returned: " + status);
    this.measureService.notifyNewFiles();
    console.log(this.uploader.queue);
    this.uploader.queue = [];

  }
  onErrorItem(item: FileItem, response: string, status: number, headers: ParsedResponseHeaders): any {
    this.openSnackBar("Noe gikk galt", "OK");
    let error = JSON.parse(response); //error server response
    console.error(error);
    this.uploader.queue = [];
  }



  startUploadAllFiles(){
    // TODO: send analytics event
    (<any>window).ga('send', 'event', {
      eventCategory: 'upload',
      eventLabel: 'choose',
      eventAction: 'Choose file for upload',
      eventValue: 10
    });

    this.uploader.uploadAllFiles();
  }

  changedExercise(event) {
    console.log(event.value);
    this.uploader.options.headers.map((obj) => {
      if (obj.name == "exNumber") {
        obj.value = event.value;
      }
    })
    this.canSubmit = true;
  }

  openSnackBar(message: string, action: string) {
    this.snackBar.open(message, action, {
      duration: 2000,
    });
  }


}

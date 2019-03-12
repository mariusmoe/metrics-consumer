import { Component, OnInit } from '@angular/core';
import { FileUploader } from 'ng2-file-upload';
import {FileUploaderCustom} from "../../_services/FileUploaderCustom";
import {Exercise} from "../../_models/exercise";


const URL = 'http://localhost:8080/api/xml/';



@Component({
  selector: 'app-upload-new-ex',
  templateUrl: './upload-new-ex.component.html',
  styleUrls: ['./upload-new-ex.component.css']
})
export class UploadNewExComponent implements OnInit {

  public uploader:FileUploaderCustom ;

  public canSubmit = false;

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

  constructor() {
    this.uploader = new FileUploaderCustom({
      url: URL,
      itemAlias: "uploadingFiles",
      headers: [{
        name: "exNumber",
        value: "3"
      }]
    });
  }

  ngOnInit() {
  }

  startUploadAllFiles(){

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


}

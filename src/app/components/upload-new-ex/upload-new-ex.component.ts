import { Component, OnInit } from '@angular/core';
import { FileUploader } from 'ng2-file-upload';
import {FileUploaderCustom} from "../../_services/FileUploaderCustom";


const URL = 'http://localhost:8080/api/xml/';


@Component({
  selector: 'app-upload-new-ex',
  templateUrl: './upload-new-ex.component.html',
  styleUrls: ['./upload-new-ex.component.css']
})
export class UploadNewExComponent implements OnInit {

  public uploader:FileUploaderCustom ;

  constructor() {
    this.uploader = new FileUploaderCustom({
      url: URL,
      itemAlias: "uploadingFiles"
    });
  }

  ngOnInit() {
  }

  uploadAllFiles(){
    this.uploader.uploadAllFiles();
  }
}

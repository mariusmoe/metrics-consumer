import { Component, OnInit } from '@angular/core';
import { FileUploader } from 'ng2-file-upload';


const URL = 'http://localhost:8080/api/xml/';


@Component({
  selector: 'app-upload-new-ex',
  templateUrl: './upload-new-ex.component.html',
  styleUrls: ['./upload-new-ex.component.css']
})
export class UploadNewExComponent implements OnInit {

  public uploader:FileUploader = new FileUploader({url: URL});

  constructor() { }

  ngOnInit() {
  }

}

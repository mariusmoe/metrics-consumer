import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UploadNewExComponent } from './upload-new-ex.component';

describe('UploadNewExComponent', () => {
  let component: UploadNewExComponent;
  let fixture: ComponentFixture<UploadNewExComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UploadNewExComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UploadNewExComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

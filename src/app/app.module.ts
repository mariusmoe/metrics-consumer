import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { HttpClientModule } from '@angular/common/http';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {
  MatButtonModule,
  MatCheckboxModule,
  MatToolbarModule,
  MatIconModule,
  MatMenuModule,
  MatSidenavModule, MatTooltipModule, MatCardModule, MatProgressBarModule
} from '@angular/material';
import { HomeComponent } from './components/home/home.component';

import { AppRoutingModule } from './app-routing.module';
import { NotFoundComponent } from './components/not-found/not-found.component';
import {NgxChartsModule} from "@swimlane/ngx-charts";
import { SettingsComponent } from './components/settings/settings.component';
import { AchievementsComponent } from './components/achievements/achievements.component';
import {FileUploadModule} from "ng2-file-upload";
import { UploadNewExComponent } from './components/upload-new-ex/upload-new-ex.component';


@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    NotFoundComponent,
    SettingsComponent,
    AchievementsComponent,
    UploadNewExComponent

  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    BrowserAnimationsModule,

    // Material design components
    MatButtonModule,
    MatCheckboxModule,
    MatToolbarModule,
    MatIconModule,
    MatMenuModule,
    MatSidenavModule,
    AppRoutingModule,
    NgxChartsModule,
    MatTooltipModule,
    MatCardModule,
    MatProgressBarModule,
    FileUploadModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }

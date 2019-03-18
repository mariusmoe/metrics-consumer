import { NgModule }              from '@angular/core';
import { RouterModule, Routes }  from '@angular/router';
import { NotFoundComponent } from './components/not-found/not-found.component';
import { HomeComponent } from './components/home/home.component';
import {AuthGuard} from "./_guards/auth.guard";
import {SettingsComponent} from "./components/settings/settings.component";
import {AchievementsComponent} from "./components/achievements/achievements.component";
import {UploadNewExComponent} from "./components/upload-new-ex/upload-new-ex.component";
import {WelcomeComponent} from "./components/welcome/welcome.component";



const appRoutes: Routes = [
  { path: '', component: HomeComponent, canActivate: [AuthGuard] },
  { path: 'login', component: WelcomeComponent },
  { path: 'achievements', component: AchievementsComponent, canActivate: [AuthGuard]},
  { path: 'user/login', component:  NotFoundComponent},
  { path: 'upload/:id', component:  UploadNewExComponent, canActivate: [AuthGuard]},
  { path: 'upload', component:  UploadNewExComponent, canActivate: [AuthGuard]},
  { path: 'summary/:taskId', component:  HomeComponent, canActivate: [AuthGuard]},
  { path: 'settings', component:  SettingsComponent, canActivate: [AuthGuard]},
  { path: '**', component:  NotFoundComponent}
];

@NgModule({
  imports: [
    RouterModule.forRoot(
      appRoutes,
      { enableTracing: false } // <-- debugging purposes only
    )
  ],
  exports: [
    RouterModule
  ]
})
export class AppRoutingModule {}

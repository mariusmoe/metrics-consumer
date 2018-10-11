import { NgModule }              from '@angular/core';
import { RouterModule, Routes }  from '@angular/router';
import { NotFoundComponent } from './components/not-found/not-found.component';
import { HomeComponent } from './components/home/home.component';
import {AuthGuard} from "./_guards/auth.guard";
import {SettingsComponent} from "./components/settings/settings.component";



const appRoutes: Routes = [
  { path: '', component: HomeComponent, canActivate: [AuthGuard] },
  { path: 'login', component: HomeComponent },
  { path: 'user/login', component:  NotFoundComponent},
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

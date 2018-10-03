import { NgModule }              from '@angular/core';
import { RouterModule, Routes }  from '@angular/router';
import { NotFoundComponent } from './components/not-found/not-found.component';
import { HomeComponent } from './components/home/home.component';



const appRoutes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component:  NotFoundComponent},
  { path: 'summary/:taskId', component:  HomeComponent},
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

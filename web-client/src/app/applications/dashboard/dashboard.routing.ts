import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {DashboardComponent} from './dashboard.component';
import {BrowserModule} from '@angular/platform-browser';


const routes: Routes = [
	{
		path: 'dashboard', component: DashboardComponent
	}
];

@NgModule({
	imports: [
		RouterModule.forChild(routes)
	],
	exports: [
		RouterModule
	]
})
export class DashboardRoutingModule {
}


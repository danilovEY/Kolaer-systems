import {NgModule} from '@angular/core';
import {CommonModule, HashLocationStrategy, LocationStrategy} from '@angular/common';
import {BrowserModule} from '@angular/platform-browser';
import {Routes, RouterModule} from '@angular/router';

import {HomeComponent} from './home.component';
import {DashboardComponent} from '../../../applications/dashboard/dashboard.component';
import {TestComponent} from '../../../applications/test/test.component';


const routes: Routes = [
	{
		path: 'home', component: HomeComponent,
		children: [
			{path: '', redirectTo: 'dashboard', pathMatch: 'full'},
			{path: 'dashboard', component: DashboardComponent},
			{path: 'test', component: TestComponent}
		]
	}
];

@NgModule({
	imports: [
		CommonModule,
		BrowserModule,
		RouterModule.forRoot(routes)
	],
	providers: [
		{provide: LocationStrategy, useClass: HashLocationStrategy}
	],
	exports: [
		RouterModule
	]
})
export class HomeRoutingModule {
}


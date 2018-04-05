import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {RouterModule, Routes} from '@angular/router';

import {HomeComponent} from './home.component';
import {DashboardModule} from '../../../applications/dashboard/dashboard.module';
import {TestModule} from '../../../applications/test/test.module';


const routes: Routes = [
	{
		path: 'home', component: HomeComponent, children: [
			{path: '', pathMatch: 'full', redirectTo: 'dashboard'},
			{path: '', loadChildren: () => DashboardModule},
			{path: '', loadChildren: () => TestModule}
		]
	}
];

@NgModule({
	imports: [
		BrowserModule,
		RouterModule.forChild(routes)
	],
	exports: [
		RouterModule
	]
})
export class HomeRoutingModule {

}



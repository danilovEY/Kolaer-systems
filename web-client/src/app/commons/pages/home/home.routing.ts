import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {RouterModule, Routes} from '@angular/router';

import {HomeComponent} from './home.component';
import {DashboardModule} from '../../../applications/dashboard/dashboard.module';
import {KolpassModule} from '../../../applications/kolpass/kolpass.module';
import {SettingModule} from '../setting/setting.module';
import {AuthGuardService} from '../../modules/auth/auth-guard.service';


const routes: Routes = [
	{
		path: 'home', component: HomeComponent, children: [
			{path: '', pathMatch: 'full', redirectTo: 'dashboard'},
			{path: '', loadChildren: () => DashboardModule},
			{path: '', loadChildren: () => SettingModule, canActivate: [AuthGuardService] },
			{path: '', loadChildren: () => KolpassModule, canActivate: [AuthGuardService] }
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



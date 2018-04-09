import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';

import {SettingComponent} from './setting.component';


const routes: Routes = [
	{
		path: 'setting', component: SettingComponent
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
export class SettingRoutingModule {
}


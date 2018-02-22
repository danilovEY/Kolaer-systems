import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';

import {AuthComponent} from './auth.component';
import {LoginPageComponent} from './pages/login-page/login-page.component';

const routers: Routes = [
	{
		path: '', component: AuthComponent, children: [
			{path: 'login-page', component: LoginPageComponent}
		]
	}
];

@NgModule({
	imports: [
		RouterModule.forChild(routers)
	],
	exports: [RouterModule]
})
export class AuthRouting { }

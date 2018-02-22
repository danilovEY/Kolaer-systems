import {NgModule} from '@angular/core';

import {LoginPageComponent} from './pages/login-page/login-page.component';
import {AuthRouting} from './auth.routing';
import {AuthComponent} from './auth.component';
import {CommonModule} from '@angular/common';
import { LoginFormComponent } from './component/login-form/login-form.component';

@NgModule({
	declarations: [
		AuthComponent,
		LoginPageComponent,
		LoginFormComponent
	],
	imports: [
		CommonModule,
		AuthRouting
	]
})
export class AuthModule {}

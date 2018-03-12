import {NgModule} from '@angular/core';
import {NavbarComponent} from './navbar.component';
import {AppCommonModule} from '../../app-common.module';
import {AuthModule} from '../auth/auth.module';
import {ReactiveFormsModule} from '@angular/forms';

import { NavbarModule as MdbNavbarModule, BsDropdownModule, ModalModule, ActiveModule, RippleModule } from 'angular-bootstrap-md'

@NgModule({
	imports: [
		AppCommonModule,
		ReactiveFormsModule,
		MdbNavbarModule,
		BsDropdownModule.forRoot(),
		ModalModule.forRoot(),
		ActiveModule.forRoot(),
		RippleModule,
		AuthModule
	],
	declarations: [
		NavbarComponent
	],
	exports: [
		NavbarComponent
	]
})
export class NavbarModule {
}

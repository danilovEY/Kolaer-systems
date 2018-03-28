import {NgModule} from '@angular/core';
import {NavbarComponent} from './navbar.component';
import {ReactiveFormsModule} from '@angular/forms';

import {AppCoreModule} from '../../app-core.module';

import {
    ActiveModule,
    BsDropdownModule,
    ModalModule,
    NavbarModule as MdbNavbarModule,
    RippleModule
} from 'angular-bootstrap-md'

@NgModule({
	imports: [
		AppCoreModule,
		ReactiveFormsModule,
		MdbNavbarModule,
		BsDropdownModule.forRoot(),
		ModalModule.forRoot(),
		ActiveModule.forRoot(),
		RippleModule
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

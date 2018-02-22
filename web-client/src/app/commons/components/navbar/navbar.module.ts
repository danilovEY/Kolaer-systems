import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {NavbarComponent} from './navbar.component';
import {ModalModule} from '../modal/modal.module';

@NgModule({
	imports: [
		CommonModule,
		ModalModule
	],
	declarations: [
		NavbarComponent
	]
})
export class NavbarModule {
}

import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {NavbarComponent} from './navbar.component';
import {ModalComponent} from '../../components/modal/modal.component';

@NgModule({
	imports: [
		CommonModule
	],
	declarations: [
		NavbarComponent,
		ModalComponent
	],
	exports: [
		NavbarComponent
	]
})
export class NavbarModule {
}

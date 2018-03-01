import {NgModule} from '@angular/core';
import {NavbarComponent} from './navbar.component';
import {AppCommonModule} from '../common/app-common.module';
import {MdbNavbarModule} from '../mdb/navbars/navbar.module';
import {MdbBsDropdownModule} from '../mdb/dropdown/dropdown.module';

@NgModule({
	imports: [
		AppCommonModule,
		MdbNavbarModule,
		MdbBsDropdownModule.forRoot()
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

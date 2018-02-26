import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {HomeRoutingModule} from './home.routing';

import {HomeComponent} from './home.component';
import {FooterModule} from '../../modules/footer/footer.module';
import {NavbarModule} from '../../modules/navbar/navbar.module';
import {SidebarModule} from '../../modules/sidebar/sidebar.module';

@NgModule({
	imports: [
		CommonModule,
		FooterModule,
		NavbarModule,
		SidebarModule,
		HomeRoutingModule
	],
	declarations: [
		HomeComponent
	]
})
export class HomeModule {
}

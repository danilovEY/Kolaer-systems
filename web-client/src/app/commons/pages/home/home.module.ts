import {NgModule} from '@angular/core';

import {HomeRoutingModule} from './home.routing';

import {HomeComponent} from './home.component';
import {FooterModule} from '../../modules/footer/footer.module';
import {SidebarModule} from '../../modules/sidebar/sidebar.module';
import {NavbarModule} from '../../modules/navbar/navbar.module';
import {BrowserModule} from '@angular/platform-browser';

@NgModule({
	imports: [
		BrowserModule,
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

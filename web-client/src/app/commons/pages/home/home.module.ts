import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule} from '@angular/router';

import {HomeRoutingModule} from './home.routing';

import {HomeComponent} from './home.component';
import {DashboardComponent} from '../../../applications/dashboard/dashboard.component';
import {FooterComponent} from '../../components/footer/footer.component';
import {NavbarComponent} from '../../components/navbar/navbar.component';
import {SidebarComponent} from '../../components/sidebar/sidebar.component';
import {TestComponent} from '../../../applications/test/test.component';

@NgModule({
	declarations: [
		FooterComponent,
		NavbarComponent,
		SidebarComponent,
		HomeComponent,
		DashboardComponent,
		TestComponent
	],
	imports: [
		CommonModule,

		HomeRoutingModule,
		RouterModule
	]
})
export class HomeModule {
}

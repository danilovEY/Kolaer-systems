import {NgModule} from '@angular/core';
import {DashboardComponent} from './dashboard.component';
import {DashboardRoutingModule} from './dashboard.routing';
import {HttpClientModule} from '@angular/common/http';
import {DashboardService} from './dashboard.service';
import {AppCoreModule} from '../../commons/app-core.module';

@NgModule({
	declarations: [
		DashboardComponent
	],
	imports: [
		AppCoreModule,
		DashboardRoutingModule,
		HttpClientModule
	],
	providers: [
		DashboardService
	]
})
export class DashboardModule {


}

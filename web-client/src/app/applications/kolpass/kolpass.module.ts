import {NgModule} from '@angular/core';
import {KolpassComponent} from './kolpass.component';
import {KolpassRoutingModule} from './kolpass.routing';
import {HttpClientModule} from '@angular/common/http';
import {KolpassService} from './kolpass.service';
import {AppCoreModule} from '../../commons/app-core.module';

@NgModule({
	declarations: [
        KolpassComponent
	],
	imports: [
		AppCoreModule,
        KolpassRoutingModule,
		HttpClientModule
	],
	providers: [
        KolpassService
	]
})
export class KolpassModule {


}

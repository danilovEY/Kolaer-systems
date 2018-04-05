import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app.routing';

import {AppComponent} from './app.component';
import {HomeModule} from './commons/pages/home/home.module';
import {NotFoundComponent} from './commons/pages/not-found/not-found.component';
import {LOG_LOGGER_PROVIDERS} from 'angular2-logger/core';
import {SettingModule} from './commons/pages/setting/setting.module';

@NgModule({
	declarations: [
		AppComponent,
		NotFoundComponent
	],
	imports: [
		HomeModule,
		SettingModule,
		AppRoutingModule
	],
	providers: [
		LOG_LOGGER_PROVIDERS
	],
	bootstrap: [AppComponent]
})
export class AppModule {
}

import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app.routing';

import {AppComponent} from './app.component';
import {HomeModule} from './commons/pages/home/home.module';
import {NotFoundComponent} from './commons/pages/not-found/not-found.component';
import {LOG_LOGGER_PROVIDERS} from 'angular2-logger/core';

@NgModule({
	declarations: [
		AppComponent,
		NotFoundComponent
	],
	imports: [
		HomeModule,
		AppRoutingModule,
	],
	providers: [
		LOG_LOGGER_PROVIDERS
	],
	bootstrap: [AppComponent]
})
export class AppModule {
}

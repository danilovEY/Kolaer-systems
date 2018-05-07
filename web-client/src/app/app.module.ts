import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app.routing';

import {AppComponent} from './app.component';
import {HomeModule} from './commons/pages/home/home.module';
import {NotFoundComponent} from './commons/pages/not-found/not-found.component';
import {LOG_LOGGER_PROVIDERS} from 'angular2-logger/core';
import {AppCoreModule} from './commons/app-core.module';
import {AuthModule} from './commons/modules/auth/auth.module';

@NgModule({
	declarations: [
		AppComponent,
		NotFoundComponent
	],
	imports: [
        AuthModule.forRoot(),
        AppCoreModule.forRoot(),
		HomeModule,
		AppRoutingModule
	],
	providers: [
		LOG_LOGGER_PROVIDERS
	],
	bootstrap: [AppComponent]
})
export class AppModule {

}

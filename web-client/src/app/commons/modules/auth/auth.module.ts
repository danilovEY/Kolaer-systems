import {NgModule} from '@angular/core';
import {AuthenticationRestService} from './authenticationRest.service';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {RestHttpClient} from '../../services/restHttpClient';
import {AuthenticationService} from '../../services/authenticationService';
import {TokenInterceptor} from './TokenInterceptor';

@NgModule({
	exports: [
		HttpClientModule
	],
	providers: [
		{ 
			provide: 'AuthenticationService', 
			useClass: AuthenticationRestService 
		},
		{
			provide: HTTP_INTERCEPTORS,
			useClass: TokenInterceptor,
			multi: true
		},
		RestHttpClient
	]
})
export class AuthModule {}

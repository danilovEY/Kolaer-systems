import {NgModule} from '@angular/core';
import {AuthenticationRestService} from './authenticationRest.service';
import {HttpClientModule} from '@angular/common/http';
import {RestHttpClient} from '../../services/restHttpClient';
import {AuthenticationService} from '../../services/authenticationService';

@NgModule({
	imports: [
		HttpClientModule
	],
	providers: [
		{ provide: 'AuthenticationService', useClass: AuthenticationRestService },
		RestHttpClient
	]
})
export class AuthModule {}

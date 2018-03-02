import {NgModule} from '@angular/core';
import {AuthenticationServiceRest} from './authentication.rest.service';
import {HttpClientModule} from '@angular/common/http';
import {RestHttpClient} from '../../services/restHttpClient';

@NgModule({
	imports: [
		HttpClientModule
	],
	providers: [
		AuthenticationServiceRest,
		RestHttpClient
	]
})
export class AuthModule {}

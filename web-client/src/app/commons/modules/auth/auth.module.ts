import {NgModule} from '@angular/core';
import {AuthenticationMockService} from './authenticationMock.service';
import {HttpClientModule} from '@angular/common/http';
import {RestHttpClient} from '../../services/restHttpClient';
import {AuthenticationService} from '../../services/authenticationService';

@NgModule({
	imports: [
		HttpClientModule
	],
	providers: [
		{ provide: 'AuthenticationService', useClass: AuthenticationMockService },
		RestHttpClient
	]
})
export class AuthModule {}

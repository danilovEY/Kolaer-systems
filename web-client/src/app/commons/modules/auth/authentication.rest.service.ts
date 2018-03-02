import {Injectable, OnInit} from '@angular/core';
import {environment} from '../../../../environments/environment';
import {UsernamePasswordModel} from '../../models/username.password.model';
import {AuthenticationService} from '../../services/authenticationService';
import {AuthenticationObserver} from '../../services/authenticationObserver';
import {ServerToken} from '../../models/server.token';
import {AccountModel} from '../../models/account.model';
import {Observable} from 'rxjs/Observable';
import {RestHttpClient} from '../../services/restHttpClient';
import {mergeMap, tap} from 'rxjs/operators';

@Injectable()
export class AuthenticationServiceRest implements OnInit, AuthenticationService {
	private authUrl: string = environment.publicServerUrl + '/authentication';
	private loginUrl: string = this.authUrl + '/login';
	private getAuthUserUrl: string = environment.publicServerUrl + '/user/get';

	private _isAuthentication: boolean = false;

	constructor(private _restHttpClient: RestHttpClient) {

	}

	ngOnInit(): void {

	}

	isAuthentication(): boolean {
		return this._isAuthentication;
	}

	login(login: string, password: string = '', rememberMe: boolean = true): Observable<AccountModel> {
		const userPasswordModel: UsernamePasswordModel =
			new UsernamePasswordModel(login.trim().toLocaleLowerCase(), password.trim().toLocaleLowerCase());

		return this._restHttpClient.post<ServerToken>(this.loginUrl, userPasswordModel)
			.pipe(
				tap((token: ServerToken) => {
					this._restHttpClient.token = token;
					this._isAuthentication = true;
				}),
				mergeMap((token: ServerToken) => this._restHttpClient.get<AccountModel>(this.getAuthUserUrl))
			);
	}

	logout(): Observable<any> {
		return new Observable();
	}

	registerObserver(observer: AuthenticationObserver): void {

	}

	removeObserver(observer: AuthenticationObserver): void {

	}

}

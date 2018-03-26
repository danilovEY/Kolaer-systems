///<reference path="../../../../../node_modules/rxjs/operators/catchError.d.ts"/>
import {Injectable, OnInit} from '@angular/core';
import {environment} from '../../../../environments/environment';
import {UsernamePasswordModel} from '../../models/username.password.model';
import {AuthenticationService} from '../../services/authenticationService';
import {AuthenticationObserver} from '../../services/authenticationObserver';
import {ServerToken} from '../../models/server.token';
import {AccountModel} from '../../models/account.model';
import {Observable} from 'rxjs/Observable';
import {RestHttpClient} from '../../services/restHttpClient';
import {catchError, mergeMap, tap} from 'rxjs/operators';
import {HttpErrorResponse} from '@angular/common/http';
import 'rxjs/add/observable/throw';

@Injectable()
export class AuthenticationRestService implements OnInit, AuthenticationService {
	private _authUrl: string = environment.publicServerUrl + '/authentication';
	private _loginUrl: string = this._authUrl + '/login';
	private _logoutUrl: string = this._authUrl + '/logout';
	private _getAuthUserUrl: string = environment.publicServerUrl + '/user/get';
	private _authObserversList: Array<AuthenticationObserver> = [];

	private _isAuthentication: boolean = false;
	private _accountModel: AccountModel = undefined;

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

		return this._restHttpClient.post<ServerToken>(this._loginUrl, userPasswordModel)
			.pipe(
				tap((token: ServerToken) => {
					this._restHttpClient.token = token;
					this._isAuthentication = true;
				}),
				mergeMap((token: ServerToken) =>
					this._restHttpClient
						.get<AccountModel>(this._getAuthUserUrl)
						.pipe(
							tap((accountModel: AccountModel) => {
								this._accountModel = accountModel;
								for (const observer of this._authObserversList) {
									observer.login(accountModel);
								}
							})
						)
				),
                catchError((err: HttpErrorResponse, caught) => {
                    return Observable.throw(err.error);
                }),
			);
	}

	logout(): Observable<void> {
        return this._restHttpClient.post<void>(this._logoutUrl)
			.pipe(tap((response: any) => {
                for (const observer of this._authObserversList) {
                    observer.login(this._accountModel);
                }

                this._isAuthentication = false;
                this._restHttpClient.token = undefined;
                this._accountModel = undefined;
			}));
	}

	registerObserver(observer: AuthenticationObserver): void {
        this._authObserversList.push(observer);
	}

	removeObserver(observer: AuthenticationObserver): void {
        const index: number = this._authObserversList.indexOf(observer);
        if (index > -1) {
            this._authObserversList.splice(index, 1);
        }
	}

    getAccountModel(): AccountModel {
        return this._accountModel;
    }

}

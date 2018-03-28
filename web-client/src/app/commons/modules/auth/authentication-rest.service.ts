import {Injectable} from '@angular/core';
import {environment} from '../../../../environments/environment';
import {UsernamePasswordModel} from '../../models/username-password.model';
import {AuthenticationService} from './authentication.service';
import {AuthenticationObserverService} from '../../services/authentication-observer.service';
import {ServerToken} from '../../models/server-token.model';
import {AccountModel} from '../../models/account.model';
import {Observable} from 'rxjs/Observable';
import {catchError, mergeMap, tap} from 'rxjs/operators';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import 'rxjs/add/observable/throw';
import 'rxjs/add/observable/empty';

@Injectable()
export class AuthenticationRestService implements AuthenticationService {
	private _authUrl: string = environment.publicServerUrl + '/authentication';
    private _loginUrl: string = this._authUrl + '/login';
    private _logoutUrl: string = this._authUrl + '/logout';
    private _refreshUrl: string = this._authUrl + '/refresh';
    private _getAuthUserUrl: string = environment.publicServerUrl + '/user/get';
    private _authObserversList: Array<AuthenticationObserverService> = [];
	private _isAuthentication: boolean = false;

    private _accountModel: AccountModel = undefined;
    private _token: ServerToken;
	constructor(private _httpClient: HttpClient) {

	}

    ngOnInit(): void {
        const token: string = localStorage.getItem('token');
        if (token) {
            this._token = new ServerToken(token);
            this._isAuthentication = true;
        }
    }


	isAuthentication(): boolean {
		return this._isAuthentication;
	}

	login(login: string, password: string = '', rememberMe: boolean = true): Observable<AccountModel> {
		const userPasswordModel: UsernamePasswordModel =
			new UsernamePasswordModel(login.trim().toLocaleLowerCase(), password.trim().toLocaleLowerCase());

		return this._httpClient.post<ServerToken>(this._loginUrl, userPasswordModel)
			.pipe(
				tap((token: ServerToken) => {
                    this.setToken(token);
					this._isAuthentication = true;
				}),
				mergeMap((token: ServerToken) =>
					this._httpClient
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
		if (this._isAuthentication) {
            return this._httpClient.post<void>(this._logoutUrl, undefined)
                .pipe(tap((response: any) => {
                    for (const observer of this._authObserversList) {
                        observer.login(this._accountModel);
                    }

                    this._isAuthentication = false;
                    this.setToken(undefined);
                    this._accountModel = undefined;
                }));
        } else {
			return Observable.empty<void>();
		}
	}

    refreshToken(): Observable<ServerToken> {
		if (this.isNeedRefreshToken()) {
            return this._httpClient.get<ServerToken>(this._refreshUrl)
				.pipe(
					tap((token: ServerToken) => this.setToken(token))
				);
		} else {
			return Observable.empty<ServerToken>();
		}
    }

    setToken(value: ServerToken): void {
        this._token = value;

        if (value) {
            localStorage.setItem('token', value.token);
        } else {
        	localStorage.removeItem('token');
		}
    }

    isNeedRefreshToken(): boolean {
		if (this._token) {
			const token: string = this._token.token;
			const arrayTokenSplit: string[] = token.split(':');
			if (arrayTokenSplit.length > 1) {
                const expires: number = Number(arrayTokenSplit[2]);

                return expires ? new Date().getTime() > (expires - 60000) : false;
			}
		}

		return false;
	}

	registerObserver(observer: AuthenticationObserverService): void {
        this._authObserversList.push(observer);
	}

	removeObserver(observer: AuthenticationObserverService): void {
        const index: number = this._authObserversList.indexOf(observer);
        if (index > -1) {
            this._authObserversList.splice(index, 1);
        }
	}

    getAccountModel(): AccountModel {
        return this._accountModel;
    }

    getToken(): ServerToken {
        return this._token;
    }

}

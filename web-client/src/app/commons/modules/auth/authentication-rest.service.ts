import {Injectable, Injector} from '@angular/core';
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
import {AccountService} from '../../services/account.service';
import "rxjs/add/operator/finally";
import {Router} from "@angular/router";

@Injectable()
export class AuthenticationRestService implements AuthenticationService {
	private readonly _authUrl: string = `${environment.publicServerUrl}/authentication`;
    private readonly _loginUrl: string = `${this._authUrl}/login`;
    private readonly _logoutUrl: string = `${this._authUrl}/logout`;
    private readonly _refreshUrl: string = `${this._authUrl}/refresh`;

    private _authObserversList: Array<AuthenticationObserverService> = [];
	private _isAuthentication: boolean = false;

    private _token: ServerToken;

	constructor(private _httpClient: HttpClient,
				private _accountService: AccountService,
                private _injector: Injector) {

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
						this._accountService.getCurrentAccount()
						.pipe(
							tap((accountModel: AccountModel) => {
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
		const obsLogout: Observable<void> = this._isAuthentication
			? this._httpClient.post<void>(this._logoutUrl, undefined)
			: Observable.empty<void>();

        return obsLogout.pipe(tap(() => {
        	for (const observer of this._authObserversList) {
				observer.logout();
			}

			this._isAuthentication = false;
			this.setToken(undefined);

			this._injector.get(Router).navigate(['/home']);
		}));
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
				const refreshTime: number = 518400; // мин. время обновления токена
                const expires: number = Number(arrayTokenSplit[1]);
                return expires ? new Date().getTime() > (expires - refreshTime) : false;
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

    getToken(): ServerToken {
        return this._token;
    }

}

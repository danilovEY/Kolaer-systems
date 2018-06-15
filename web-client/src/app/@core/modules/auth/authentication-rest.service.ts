import {Injectable, Injector} from '@angular/core';
import {environment} from '../../../../environments/environment';
import {UsernamePasswordModel} from '../../models/username-password.model';
import {AuthenticationObserverService} from '../../services/authentication-observer.service';
import {ServerToken} from '../../models/server-token.model';
import {EMPTY, Observable} from 'rxjs/index';
import {catchError, finalize, tap} from 'rxjs/operators';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {Router} from '@angular/router';
import {ServerExceptionModel} from '../../models/server-exception.model';

@Injectable()
export class AuthenticationRestService {
	private readonly _authUrl: string = `${environment.publicServerUrl}/authentication`;
    private readonly _loginUrl: string = `${this._authUrl}/login`;
    private readonly _logoutUrl: string = `${this._authUrl}/logout`;
    private readonly _refreshUrl: string = `${this._authUrl}/refresh`;

    public _authObserversList: Array<AuthenticationObserverService> = [];
	public authentication: boolean = false;

    private _token: ServerToken;

	constructor(private _httpClient: HttpClient,
                private _injector: Injector) {
	}

    ngOnInit(): void {
        const token: string = localStorage.getItem('token');
        if (token) {
            this._token = new ServerToken(token);
            this.authentication = true;
        }
    }

	login(login: string, password: string = '', rememberMe: boolean = true): Observable<ServerToken> {
		const userPasswordModel: UsernamePasswordModel =
			new UsernamePasswordModel(login.trim().toLocaleLowerCase(), password.trim().toLocaleLowerCase());

		return this._httpClient.post<ServerToken>(this._loginUrl, userPasswordModel)
			.pipe(
				tap((token: ServerToken) => {
                    this.setToken(token);
					this.authentication = true;

                    for (const observer of this._authObserversList) {
                        observer.login();
                    }
				}),
                catchError((err: HttpErrorResponse, caught) => {
                	const serverException: ServerExceptionModel = new ServerExceptionModel();
                    serverException.status = 0;
                    serverException.message = 'Сервер недоступен.';

                    return err.status === 0 ? Observable.throw(serverException) : Observable.throw(err.error);
                }),
			);
	}

	logout(redirectToLoginPage: boolean = false): Observable<any> {
		const obsLogout: Observable<void> = this.authentication
			? this._httpClient.post<void>(this._logoutUrl, undefined)
			: EMPTY;

        return obsLogout.pipe(finalize(() => {
        	this.authentication = false;
			this.setToken(undefined);

			for (const observer of this._authObserversList) {
				observer.logout();
			}

			if (redirectToLoginPage) {
                this._injector.get(Router).navigate(['/auth/login']);
			} else {
                this._injector.get(Router).navigate(['/']);
			}
		}));
	}

    refreshToken(): Observable<ServerToken> {
		if (this.isNeedRefreshToken()) {
            return this._httpClient.get<ServerToken>(this._refreshUrl)
				.pipe(
					tap((token: ServerToken) => this.setToken(token))
				);
		} else {
			return EMPTY;
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

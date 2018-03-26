import {Injectable, OnInit} from '@angular/core';
import {AuthenticationService} from '../../services/authenticationService';
import {Observable} from 'rxjs/Observable';
import {AccountModel} from '../../models/account.model';
import {AuthenticationObserver} from '../../services/authenticationObserver';
import 'rxjs/add/observable/of';
import {Logger} from 'angular2-logger/core';

@Injectable()
export class AuthenticationMockService implements OnInit, AuthenticationService {
	private _accountModel: AccountModel;
	private _isAuth: boolean = false;

	constructor(private _logger: Logger) {
		this.ngOnInit();
	}

	ngOnInit(): void {
		this._logger.warn('Auth service is mock');

		this._accountModel = new AccountModel();
		this._accountModel.id = 1;
		this._accountModel.username = 'test';
	}

	isAuthentication(): boolean {
		return this._isAuth;
	}

	login(login: string, password?: string, rememberMe?: boolean): Observable<AccountModel> {
		this._isAuth = true;
		return Observable.of(this._accountModel);
	}

	logout(): Observable<any> {
		this._isAuth = false;
		return Observable.of(null);
	}

	registerObserver(observer: AuthenticationObserver): void {
	}

	removeObserver(observer: AuthenticationObserver): void {
	}

    getAccountModel(): AccountModel {
        return undefined;
    }

}

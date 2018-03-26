import {AuthenticationObserver} from './authenticationObserver';
import {AccountModel} from '../models/account.model';
import {Observable} from 'rxjs/Observable';

export interface AuthenticationService {
	isAuthentication(): boolean;
	login(login: string, password?: string, rememberMe?: boolean): Observable<AccountModel>
	logout(): Observable<any>

	registerObserver(observer: AuthenticationObserver): void;
	removeObserver(observer: AuthenticationObserver): void;

    getAccountModel(): AccountModel;
}

import {AuthenticationObserverService} from '../../services/authentication-observer.service';
import {AccountModel} from '../../models/account.model';
import {Observable} from 'rxjs/Observable';
import {ServerToken} from "../../models/server-token.model";
import {OnInit} from "@angular/core";

export interface AuthenticationService extends OnInit {
	isAuthentication(): boolean;
	login(login: string, password?: string, rememberMe?: boolean): Observable<AccountModel>
	logout(): Observable<any>

	registerObserver(observer: AuthenticationObserverService): void;
	removeObserver(observer: AuthenticationObserverService): void;

	refreshToken(): Observable<ServerToken>;

    getAccountModel(): AccountModel;
    getToken(): ServerToken;
}

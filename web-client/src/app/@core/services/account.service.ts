import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../environments/environment';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/observable/of';
import {tap} from 'rxjs/operators';
import {AuthenticationRestService} from '../modules/auth/authentication-rest.service';
import {AuthenticationObserverService} from './authentication-observer.service';
import {SimpleAccountModel} from "../models/simple-account.model";

@Injectable()
export class AccountService implements AuthenticationObserverService {
    private _getAuthUserUrl: string = environment.publicServerUrl + '/user';

    private _currentAccountModel: SimpleAccountModel = undefined;

    constructor(private _httpClient: HttpClient,
                private _authService: AuthenticationRestService) {
        this._authService.registerObserver(this);
    }

    login(): void {
    }

    logout(): void {
        this._currentAccountModel = undefined;
    }
    
    getCurrentAccount(cache: boolean = true): Observable<SimpleAccountModel> {
        if (cache && this._currentAccountModel || !this._authService.authentication) {
            return Observable.of(this._currentAccountModel);
        } else {
            return this._httpClient.get<SimpleAccountModel>(this._getAuthUserUrl)
                .pipe(tap((account: SimpleAccountModel) => this._currentAccountModel = account))
                .catch(error => {
                    if (error.status === 403 || error.status === 401) {
                        this._authService.logout().subscribe(Observable.empty);
                    }
                    return Observable.throw(error);
                });
        }
    }
}

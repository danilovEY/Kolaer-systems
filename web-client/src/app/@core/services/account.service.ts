import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {AccountModel} from '../models/account.model';
import {environment} from '../../../environments/environment';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/observable/of';
import {catchError, tap} from 'rxjs/operators';
import {AuthenticationRestService} from '../modules/auth/authentication-rest.service';
import {AuthenticationObserverService} from './authentication-observer.service';

@Injectable()
export class AccountService implements AuthenticationObserverService {
    private _getAuthUserUrl: string = environment.publicServerUrl + '/user/get';

    private _currentAccountModel: AccountModel = undefined;

    constructor(private _httpClient: HttpClient,
                private _authService: AuthenticationRestService) {
        this._authService.registerObserver(this);
    }

    login(): void {
    }

    logout(): void {
        this._currentAccountModel = undefined;
    }
    
    getCurrentAccount(): Observable<AccountModel> {
        if (this._currentAccountModel) {
            return Observable.of(this._currentAccountModel);
        } else {
            return this._httpClient.get<AccountModel>(this._getAuthUserUrl)
                .pipe(
                    catchError(error => {
                        if (error.status === 403 || error.status === 401) {
                           this._authService.logout().subscribe(Observable.empty);
                        }
                        return error;
                    }),
                    tap((account: AccountModel) => this._currentAccountModel = account)
                );
        }
    }
}

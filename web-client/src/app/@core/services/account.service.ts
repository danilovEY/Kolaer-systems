import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {environment} from '../../../environments/environment';
import {Observable, of, throwError} from 'rxjs/index';
import {catchError, tap} from 'rxjs/operators';
import {AuthenticationRestService} from '../modules/auth/authentication-rest.service';
import {AuthenticationObserverService} from './authentication-observer.service';
import {SimpleAccountModel} from '../models/simple-account.model';
import {Page} from '../models/page.model';
import {AccountSortModel} from '../models/account-sort.model';
import {AccountFilterModel} from '../models/account-filter.model';
import {AccountModel} from '../models/account.model';
import {BaseService} from './base.service';

@Injectable()
export class AccountService extends BaseService implements AuthenticationObserverService {
    private readonly _getAuthUserUrl: string = environment.publicServerUrl + '/user';
    private readonly _getAccountsUrl: string = environment.publicServerUrl + '/accounts';

    private _currentAccountModel: SimpleAccountModel = undefined;

    constructor(private _httpClient: HttpClient,
                private _authService: AuthenticationRestService) {
        super();
        this._authService.registerObserver(this);
    }

    login(): void {
    }

    logout(): void {
        this._currentAccountModel = undefined;
    }
    
    getCurrentAccount(cache: boolean = true): Observable<SimpleAccountModel> {
        if (cache && this._currentAccountModel || !this._authService.authentication) {
            return of(this._currentAccountModel);
        } else {
            return this._httpClient.get<SimpleAccountModel>(this._getAuthUserUrl)
                .pipe(tap((account: SimpleAccountModel) => this._currentAccountModel = account),
                    catchError(error => {
                        if (error.status === 403 || error.status === 401) {
                            this._authService.logout().subscribe();
                        }
                        return throwError(error);
                    }));
        }
    }

    getAllAccounts(sort?: AccountSortModel, filter?: AccountFilterModel,
                    page: number = 1, pageSize: number = 15): Observable<Page<AccountModel>> {
        let params = new HttpParams();

        params = params.append('page', page.toString()).append('pagesize', pageSize.toString());
        params = this.getSortAndFilterParam(params, sort, filter);

        return this._httpClient.get<Page<AccountModel>>(this._getAccountsUrl, {params: params});
    }
}

import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {AccountModel} from '../models/account.model';
import {environment} from '../../../environments/environment';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/observable/of';
import {tap} from 'rxjs/operators';

@Injectable()
export class AccountService {
    private _getAuthUserUrl: string = environment.publicServerUrl + '/user/get';

    private _currentAccountModel: AccountModel = undefined;

    constructor(private _httpClient: HttpClient) {
        
    }
    
    getCurrentAccount(): Observable<AccountModel> {
        if (this._currentAccountModel) {
            return Observable.of(this._currentAccountModel);
        } else {
            return this._httpClient.get<AccountModel>(this._getAuthUserUrl)
                .pipe(
                    tap((account: AccountModel) => this._currentAccountModel = account)
                );
        }
    }
}

import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../environments/environment';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/observable/of';
import {switchMap, tap} from 'rxjs/operators';
import {AuthenticationRestService} from '../modules/auth/authentication-rest.service';
import {AuthenticationObserverService} from './authentication-observer.service';
import {SimpleAccountModel} from '../models/simple-account.model';
import {EmployeeModel} from '../models/employee.model';
import {AccountService} from './account.service';

@Injectable()
export class EmployeeService implements AuthenticationObserverService {
    private _getEmployeeUrl: string = environment.publicServerUrl + '/employees';

    private _currentEmployeeModel: EmployeeModel = undefined;

    constructor(private _httpClient: HttpClient,
                private _authService: AuthenticationRestService,
                private accountService: AccountService) {
        this._authService.registerObserver(this);
    }

    login(): void {
    }

    logout(): void {
        this._currentEmployeeModel = undefined;
    }
    
    getCurrentEmployee(cache: boolean = true): Observable<EmployeeModel> {
        if (cache && this._currentEmployeeModel) {
            return Observable.of(this._currentEmployeeModel);
        } else {
            return this.accountService.getCurrentAccount().pipe(
                switchMap((account: SimpleAccountModel) =>
                    account.employeeId
                        ? this._httpClient.get<EmployeeModel>(this._getEmployeeUrl + `/${account.employeeId}`)
                        : Observable.empty()
                ),
                tap((employee: EmployeeModel) => this._currentEmployeeModel = employee)
            ).catch(error => {
                if (error.status === 403 || error.status === 401) {
                    this._authService.logout().subscribe(Observable.empty);
                }
                return Observable.throw(error);
            });
        }
    }
}

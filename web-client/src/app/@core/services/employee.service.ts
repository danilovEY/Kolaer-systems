import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {environment} from '../../../environments/environment';
import {EMPTY, Observable, of} from 'rxjs/index';
import {AuthenticationRestService} from '../modules/auth/authentication-rest.service';
import {AuthenticationObserverService} from './authentication-observer.service';
import {SimpleAccountModel} from '../models/simple-account.model';
import {EmployeeModel} from '../models/employee.model';
import {AccountService} from './account.service';
import {OtherEmployeeModel} from '../models/other-employee.model';
import {Page} from '../models/page.model';
import {EmployeeSortModel} from '../models/employee-sort.model';
import {EmployeeFilterModel} from '../models/employee-filter.model';
import {BaseService} from './base.service';
import {EmployeeRequestModel} from '../models/employee-request.model';
import {catchError, switchMap, tap} from 'rxjs/internal/operators';

@Injectable()
export class EmployeeService extends BaseService implements AuthenticationObserverService {
    private readonly getEmployeeUrl: string = environment.publicServerUrl + '/employees';
    private readonly getAllEmployeesUrl: string = `${this.getEmployeeUrl}/get/all`;
    private readonly getAllEmployeeBirthdayToday: string = `${this.getEmployeeUrl}/get/birthday/today`;
    private readonly getAllOtherEmployeeBirthdayToday: string =
        `${environment.publicServerUrl}/organizations/employees/get/users/birthday/today`;

    private _currentEmployeeModel: EmployeeModel = undefined;

    static getShortNameOrganization(organization: string): string {
        switch (organization) {
            case 'БалаковоАтомэнергоремонт': return 'БалАЭР';
            case 'ВолгодонскАтомэнергоремонт': return 'ВДАЭР';
            case 'КалининАтомэнергоремонт': return 'КАЭР';
            case 'КурскАтомэнергоремонт': return 'КурскАЭР';
            case 'ЛенАтомэнергоремонт': return 'ЛенАЭР';
            case 'НововоронежАтомэнергоремонт': return 'НВАЭР';
            case 'СмоленскАтомэнергоремонт': return 'САЭР';
            case 'УралАтомэнергоремонт': return 'УралАЭР';
            case 'Центральный аппарат': return 'ЦА';
            case 'КолАтомэнергоремонт': return 'КолАЭР';
            default: return organization;
        }
    }

    constructor(private _httpClient: HttpClient,
                private _authService: AuthenticationRestService,
                private accountService: AccountService) {
        super();
        this._authService.registerObserver(this);
    }


    login(): void {
    }

    logout(): void {
        this._currentEmployeeModel = undefined;
    }

    getCurrentEmployee(cache: boolean = true): Observable<EmployeeModel> {
        if (cache && this._currentEmployeeModel) {
            return of(this._currentEmployeeModel);
        } else {
            return this.accountService.getCurrentAccount().pipe(
                switchMap((account: SimpleAccountModel) =>
                    account.employeeId
                        ? this._httpClient.get<EmployeeModel>(this.getEmployeeUrl + `/${account.employeeId}`)
                        : EMPTY
                ),
                tap((employee: EmployeeModel) => this._currentEmployeeModel = employee),
                catchError(error => {
                    if (error.status === 403 || error.status === 401) {
                        this._authService.logout().subscribe();
                    }
                    return Observable.throw(error);
                }));
        }
    }

    getAllEmployees(sort?: EmployeeSortModel, filter?: EmployeeFilterModel,
                    page: number = 1, pageSize: number = 15): Observable<Page<EmployeeModel>> {
        let params = new HttpParams();

        params = params.append('page', page.toString()).append('pagesize', pageSize.toString());
        params = this.getSortAndFilterParam(params, sort, filter);

        return this._httpClient.get<Page<EmployeeModel>>(this.getAllEmployeesUrl, {params: params});
    }

    getEmployeesBirthdayToday(): Observable<EmployeeModel[]> {
        return this._httpClient.get<EmployeeModel[]>(this.getAllEmployeeBirthdayToday)
            .pipe(tap((employees: EmployeeModel[]) => {
                for (const emp of employees) {
                    if (emp.birthday) {
                        emp.birthday = new Date(emp.birthday);
                    }

                    if (emp.dismissalDate) {
                        emp.dismissalDate = new Date(emp.dismissalDate);
                    }

                    if (emp.employmentDate) {
                        emp.employmentDate = new Date(emp.employmentDate);
                    }
                }
            }));
    }

    getOtherEmployeesBirthdayToday(): Observable<OtherEmployeeModel[]> {
        return this._httpClient.get<OtherEmployeeModel[]>(this.getAllOtherEmployeeBirthdayToday)
            .pipe(tap((employees: OtherEmployeeModel[]) => {
                for (const emp of employees) {
                    if (emp.birthday) {
                        emp.birthday = new Date(emp.birthday);
                    }
                }
                return employees;
            }));
    }

    updateEmployee(employeeId: number, employee: EmployeeRequestModel): Observable<EmployeeModel> {
        const url = `${this.getEmployeeUrl}/${employeeId}`;

        return this._httpClient.put<EmployeeModel>(url, employee);
    }

    createEmployee(employee: EmployeeRequestModel): Observable<EmployeeModel> {
        return this._httpClient.post<EmployeeModel>(this.getEmployeeUrl, employee);
    }

    deleteEmployee(employeeId: number): Observable<any> {
        const url = `${this.getEmployeeUrl}/${employeeId}`;

        return this._httpClient.delete(url);
    }

}

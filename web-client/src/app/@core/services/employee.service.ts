import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {environment} from '../../../environments/environment';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/observable/of';
import {switchMap, tap} from 'rxjs/operators';
import {AuthenticationRestService} from '../modules/auth/authentication-rest.service';
import {AuthenticationObserverService} from './authentication-observer.service';
import {SimpleAccountModel} from '../models/simple-account.model';
import {EmployeeModel} from '../models/employee.model';
import {AccountService} from './account.service';
import {OtherEmployeeModel} from '../models/other-employee.model';
import {Page} from "../models/page.model";
import {EmployeeSortModel} from "../models/employee-sort.model";
import {EmployeeFilterModel} from "../models/employee-filter.model";

@Injectable()
export class EmployeeService implements AuthenticationObserverService {
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
                        ? this._httpClient.get<EmployeeModel>(this.getEmployeeUrl + `/${account.employeeId}`)
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

    getAllEmployees(sort?: EmployeeSortModel, filter?: EmployeeFilterModel,
                    page: number = 1, pageSize: number = 15): Observable<Page<EmployeeModel>> {
        let params = new HttpParams();

        params = params.append('page', page.toString())
            .append('pagesize', pageSize.toString());
        
        if (sort) {
            params = sort.sortId ? params.append('sortId', sort.sortId) : params;
            params = sort.sortInitials ? params.append('sortInitials', sort.sortInitials) : params;
            params = sort.sortPostName ? params.append('sortPostName', sort.sortPostName) : params;
            params = sort.sortDepartmentName ? params.append('sortDepartmentName', sort.sortDepartmentName) : params;
        }

        if (filter) {
            params = filter.filterId ? params.append('filterId', filter.filterId.toString()) : params;
            params = filter.filterInitials ? params.append('filterInitials', filter.filterInitials) : params;
            params = filter.filterPostName ? params.append('filterPostName', filter.filterPostName) : params;
            params = filter.filterDepartmentName ? params.append('filterDepartmentName', filter.filterDepartmentName) : params;
        }

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

}

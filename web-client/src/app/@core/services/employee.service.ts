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
import {catchError, map, switchMap, tap} from 'rxjs/internal/operators';
import {HistoryChangeModel} from "../models/history-change.model";
import {HistoryChangeEventEnum} from "../models/history-change-event.enum";

@Injectable()
export class EmployeeService extends BaseService implements AuthenticationObserverService {
    private readonly getEmployeeUrl: string = environment.publicServerUrl + '/employees';
    private readonly getAllEmployeesUrl: string = `${this.getEmployeeUrl}/get/all`;
    private readonly uploadEmployeesUrl: string = `${this.getEmployeeUrl}/sync`;
    private readonly sendReportForOldDbUrl: string = `${this.getEmployeeUrl}/old/report`;
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
                tap((employee: EmployeeModel) => this._currentEmployeeModel = this.convertModel(employee)),
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

        return this._httpClient.get<Page<EmployeeModel>>(this.getAllEmployeesUrl, {params: params})
            .pipe(tap((request: Page<EmployeeModel>) => request.data.map(this.convertModel)));
    }

    getEmployeesBirthdayToday(): Observable<EmployeeModel[]> {
        return this._httpClient.get<EmployeeModel[]>(this.getAllEmployeeBirthdayToday)
            .pipe(tap((employees: EmployeeModel[]) => employees.map(this.convertModel)));
    }

    getOtherEmployeesBirthdayToday(): Observable<OtherEmployeeModel[]> {
        return this._httpClient.get<OtherEmployeeModel[]>(this.getAllOtherEmployeeBirthdayToday)
            .pipe(tap((employees: OtherEmployeeModel[]) => employees.map(this.convertToOtherEmployeeModel)));
    }

    updateEmployee(employeeId: number, employee: EmployeeRequestModel): Observable<EmployeeModel> {
        const url = `${this.getEmployeeUrl}/${employeeId}`;

        return this._httpClient.put<EmployeeModel>(url, employee)
            .pipe(map((request: EmployeeModel) => this.convertModel(request)));
    }

    createEmployee(employee: EmployeeRequestModel): Observable<EmployeeModel> {
        return this._httpClient.post<EmployeeModel>(this.getEmployeeUrl, employee)
            .pipe(map((request: EmployeeModel) => this.convertModel(request)));
    }

    deleteEmployee(employeeId: number): Observable<any> {
        const url = `${this.getEmployeeUrl}/${employeeId}`;

        return this._httpClient.delete(url);
    }


    convertModel(model: EmployeeModel): EmployeeModel {
        model.birthday =  model.birthday ? new Date(model.birthday) : null;
        model.dismissalDate =  model.dismissalDate ? new Date(model.dismissalDate) : null;
        model.employmentDate =  model.employmentDate ? new Date(model.employmentDate) : null;

        return model;
    }

    convertToOtherEmployeeModel(model: OtherEmployeeModel): OtherEmployeeModel {
        model.birthday =  model.birthday ? new Date(model.birthday) : null;

        return model;
    }

    uploadFileForSync(file: File): Observable<HistoryChangeModel[]> {
        const formData: FormData = new FormData();
        formData.append('file', file);

        return this._httpClient.post(this.uploadEmployeesUrl, formData)
            .pipe(map((request: HistoryChangeModel[]) => request.map(this.convertToHistoryChangeModel)));
    }

    sendReportForOldDb(): Observable<any> {
        return this._httpClient.post(this.sendReportForOldDbUrl, null);
    }

    convertToHistoryChangeModel(model: HistoryChangeModel): HistoryChangeModel {
        model.eventDate = model.eventDate ? new Date(model.eventDate) : null;
        model.event = model.event ? HistoryChangeEventEnum[model.event] : null;

        return model;
    }
}

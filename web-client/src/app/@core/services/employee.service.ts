import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
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
import {FindEmployeeRequestModel} from '../models/find-employee-request.model';
import {HistoryChangeModel} from '../models/history-change.model';
import {HistoryChangeEventEnum} from '../models/history-change-event.enum';
import {UpdateTypeWorkEmployeeRequestModel} from '../models/update-type-work-employee-request.model';
import {PathVariableConstant} from "../constants/path-variable.constant";
import {RouterServiceConstant} from "../constants/router-service.constant";
import {Utils} from "../utils/utils";

@Injectable()
export class EmployeeService extends BaseService implements AuthenticationObserverService {

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
                        ? this._httpClient.get<EmployeeModel>(
                            Utils.createUrlFromUrlTemplate(
                                RouterServiceConstant.EMPLOYEES_ID_URL,
                                PathVariableConstant.EMPLOYEE_ID,
                                account.employeeId.toString()
                            )
                        )
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
                    pageNum: number = 1, pageSize: number = 15): Observable<Page<EmployeeModel>> {
        let params = new HttpParams();

        params = params.append('pageNum', pageNum.toString()).append('pageSize', pageSize.toString());
        params = this.getSortAndFilterParam(params, sort, filter);

        return this._httpClient.get<Page<EmployeeModel>>(RouterServiceConstant.EMPLOYEES_ALL_URL, {params: params})
            .pipe(tap((request: Page<EmployeeModel>) => request.data.map(this.convertModel)));
    }

    findAllEmployees(findRequest: FindEmployeeRequestModel): Observable<Page<EmployeeModel>> {
        let params = new HttpParams();

        params = params.append('pageNum', String(findRequest.pageNum))
            .append('query', findRequest.query)
            .append('pageSize', String(findRequest.pageSize))
            .append('onOnePage', String(findRequest.onOnePage))
            .append('departmentIds', findRequest.departmentIds.toString());

        return this._httpClient.get<Page<EmployeeModel>>(RouterServiceConstant.EMPLOYEES_URL, {params: params})
            .pipe(tap((request: Page<EmployeeModel>) => request.data.forEach(this.convertModel)));
    }

    getEmployeesBirthdayToday(): Observable<EmployeeModel[]> {
        return this._httpClient.get<EmployeeModel[]>(RouterServiceConstant.EMPLOYEES_ALL_BIRTHDAY)
            .pipe(tap((employees: EmployeeModel[]) => employees.map(this.convertModel)));
    }

    getOtherEmployeesBirthdayToday(): Observable<OtherEmployeeModel[]> {
        return this._httpClient.get<OtherEmployeeModel[]>(RouterServiceConstant.OTHER_EMPLOYEES_ALL_BIRTHDAY)
            .pipe(tap((employees: OtherEmployeeModel[]) => employees.map(this.convertToOtherEmployeeModel)));
    }

    getEmployeeById(employeeId: number): Observable<EmployeeModel> {
        const url = Utils.createUrlFromUrlTemplate(
            RouterServiceConstant.EMPLOYEES_ID_URL,
            PathVariableConstant.EMPLOYEE_ID,
            employeeId.toString()
        );

        return this._httpClient.get<EmployeeModel>(url)
            .pipe(map((response: EmployeeModel) => this.convertModel(response)));
    }

    updateEmployee(employeeId: number, employee: EmployeeRequestModel): Observable<EmployeeModel> {
        const url = Utils.createUrlFromUrlTemplate(
            RouterServiceConstant.EMPLOYEES_ID_URL,
            PathVariableConstant.EMPLOYEE_ID,
            employeeId.toString()
        );

        return this._httpClient.put<EmployeeModel>(url, employee)
            .pipe(map((response: EmployeeModel) => this.convertModel(response)));
    }

    updateTypeWorkEmployee(employeeId: number, request: UpdateTypeWorkEmployeeRequestModel): Observable<EmployeeModel> {
        const url = Utils.createUrlFromUrlTemplate(
            RouterServiceConstant.EMPLOYEES_ID_TYPE_WORK_URL,
            PathVariableConstant.EMPLOYEE_ID,
            employeeId.toString()
        );

        return this._httpClient.put<EmployeeModel>(url, request)
            .pipe(map((response: EmployeeModel) => this.convertModel(response)));
    }

    createEmployee(employee: EmployeeRequestModel): Observable<EmployeeModel> {
        return this._httpClient.post<EmployeeModel>(RouterServiceConstant.EMPLOYEES_URL, employee)
            .pipe(map((response: EmployeeModel) => this.convertModel(response)));
    }

    deleteEmployee(employeeId: number): Observable<any> {
        const url = Utils.createUrlFromUrlTemplate(
            RouterServiceConstant.EMPLOYEES_ID_URL,
            PathVariableConstant.EMPLOYEE_ID,
            employeeId.toString()
        );

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

        return this._httpClient.post(RouterServiceConstant.EMPLOYEES_SYNC, formData)
            .pipe(map((request: HistoryChangeModel[]) => request.map(this.convertToHistoryChangeModel)));
    }

    sendReportForOldDb(): Observable<any> {
        return this._httpClient.post(RouterServiceConstant.EMPLOYEES_OLD_REPORT, null);
    }

    convertToHistoryChangeModel(model: HistoryChangeModel): HistoryChangeModel {
        model.eventDate = model.eventDate ? new Date(model.eventDate) : null;
        model.event = model.event ? HistoryChangeEventEnum[model.event] : null;

        return model;
    }
}

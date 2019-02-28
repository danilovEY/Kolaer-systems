import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs/index';
import 'rxjs/add/observable/of';
import {Page} from '../models/page.model';
import {BaseService} from './base.service';
import {DepartmentModel} from '../models/department/department.model';
import {DepartmentRequestModel} from '../models/department-request.model';
import {FindDepartmentPageRequest} from '../models/department/find-department-page-request';
import {RouterServiceConstant} from "../constants/router-service.constant";
import {Utils} from "../utils/utils";
import {PathVariableConstant} from "../constants/path-variable.constant";

@Injectable()
export class DepartmentService extends BaseService {

    constructor(private _httpClient: HttpClient) {
        super();
    }

    createDepartment(department: DepartmentRequestModel): Observable<DepartmentModel> {
        return this._httpClient.post<DepartmentModel>(RouterServiceConstant.DEPARTMENTS_URL, department);
    }

    updateDepartment(depId: number, department: DepartmentRequestModel): Observable<DepartmentModel> {
        const url = Utils.createUrlFromUrlTemplate(
            RouterServiceConstant.DEPARTMENTS_ID_URL,
            PathVariableConstant.DEPARTMENT_ID,
            depId.toString()
        );

        return this._httpClient.put<DepartmentModel>(url, department);
    }

    deleteDepartment(depId: number): Observable<any> {
        const url = Utils.createUrlFromUrlTemplate(
            RouterServiceConstant.DEPARTMENTS_ID_URL,
            PathVariableConstant.DEPARTMENT_ID,
            depId.toString()
        );

        return this._httpClient.delete(url);
    }

    find(request: FindDepartmentPageRequest): Observable<Page<DepartmentModel>> {
        const params = this.includeHttpParams(new HttpParams(), request);

        return this._httpClient.get<Page<DepartmentModel>>(RouterServiceConstant.DEPARTMENTS_URL, {params: params});
    }
}

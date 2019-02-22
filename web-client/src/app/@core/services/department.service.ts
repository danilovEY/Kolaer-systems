import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {environment} from '../../../environments/environment';
import {Observable} from 'rxjs/index';
import 'rxjs/add/observable/of';
import {Page} from '../models/page.model';
import {BaseService} from './base.service';
import {DepartmentModel} from '../models/department.model';
import {DepartmentSortModel} from '../models/department-sort.model';
import {DepartmentFilterModel} from '../models/department-filter.model';
import {DepartmentRequestModel} from '../models/department-request.model';
import {FindDepartmentPageRequest} from '../models/department/find-department-page-request';

@Injectable()
export class DepartmentService extends BaseService {
    private readonly getDepartmentUrl: string = environment.publicServerUrl + '/departments';
    private readonly FIND_DEPARTMENTS_URL: string = this.getDepartmentUrl + '/find';

    constructor(private _httpClient: HttpClient) {
        super();
    }

    getAllDepartments(sort?: DepartmentSortModel, filter?: DepartmentFilterModel,
                    pageNum: number = 1, pageSize: number = 15): Observable<Page<DepartmentModel>> {
        let params = new HttpParams();

        params = params.append('pageNum', pageNum.toString()).append('pagesize', pageSize.toString());
        params = this.getSortAndFilterParam(params, sort, filter);

        return this._httpClient.get<Page<DepartmentModel>>(this.getDepartmentUrl, {params: params});
    }

    createDepartment(department: DepartmentRequestModel): Observable<DepartmentModel> {
        return this._httpClient.post<DepartmentModel>(this.getDepartmentUrl, department);
    }

    updateDepartment(depId: number, department: DepartmentRequestModel): Observable<DepartmentModel> {
        const url = `${this.getDepartmentUrl}/${depId}`;
        return this._httpClient.put<DepartmentModel>(url, department);
    }

    deleteDepartment(depId: number): Observable<any> {
        const url = `${this.getDepartmentUrl}/${depId}`;
        return this._httpClient.delete(url);
    }

    find(request: FindDepartmentPageRequest): Observable<Page<DepartmentModel>> {
        let params = new HttpParams();

        params = params.append('pageNum', String(request.pageNum))
            .append('pagesize', String(request.pageSize))
            .append('onOnePage', String(request.onOnePage))
            .append('query', request.query);

        return this._httpClient.get<Page<DepartmentModel>>(this.FIND_DEPARTMENTS_URL, {params: params});
    }
}

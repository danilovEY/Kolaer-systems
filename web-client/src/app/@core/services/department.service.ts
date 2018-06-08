import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {environment} from '../../../environments/environment';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/observable/of';
import {Page} from '../models/page.model';
import {BaseService} from './base.service';
import {DepartmentModel} from '../models/department.model';
import {DepartmentSortModel} from '../models/department-sort.model';
import {DepartmentFilterModel} from '../models/department-filter.model';

@Injectable()
export class DepartmentService extends BaseService {
    private readonly getDepartmentUrl: string = environment.publicServerUrl + '/departments';

    constructor(private _httpClient: HttpClient) {
        super();
    }

    getAllDepartments(sort?: DepartmentSortModel, filter?: DepartmentFilterModel,
                    page: number = 1, pageSize: number = 15): Observable<Page<DepartmentModel>> {
        let params = new HttpParams();

        params = params.append('page', page.toString()).append('pagesize', pageSize.toString());
        params = this.getSortAndFilterParam(params, sort, filter);

        return this._httpClient.get<Page<DepartmentModel>>(this.getDepartmentUrl, {params: params});
    }
}

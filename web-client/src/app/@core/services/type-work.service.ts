import {BaseService} from './base.service';
import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs/Rx';
import {TypeWorkModel} from '../models/type-work.model';
import {Page} from '../models/page.model';
import {environment} from '../../../environments/environment';
import {FindTypeWorkRequest} from '../models/find-type-work-request';

@Injectable()
export class TypeWorkService extends BaseService {
    private readonly getTypeWorksUrl = `${environment.publicServerUrl}/type-work`;

    constructor(private http: HttpClient) {
        super();
    }

    getAll(request: FindTypeWorkRequest): Observable<Page<TypeWorkModel>> {
        const params: HttpParams = new HttpParams()
            .append('searchName', request.searchName ? request.searchName.toString() : '')
            .append('departmentIds', request.departmentIds.toString())
            .append('number', request.number.toString())
            .append('pagesize', request.pageSize.toString());

        return this.http.get<Page<TypeWorkModel>>(this.getTypeWorksUrl, {params: params});
    }

    add(request: TypeWorkModel): Observable<TypeWorkModel> {
        return this.http.post<TypeWorkModel>(this.getTypeWorksUrl, request);
    }

    update(typeWorkId: number, request: TypeWorkModel): Observable<TypeWorkModel> {
        const url = `${this.getTypeWorksUrl}/${typeWorkId}`;
        return this.http.put<TypeWorkModel>(url, request);
    }

    delete(typeWorkId: number): Observable<Object> {
        const url = `${this.getTypeWorksUrl}/${typeWorkId}`;
        return this.http.delete<Object>(url);
    }

}

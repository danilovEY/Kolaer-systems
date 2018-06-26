import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs/Rx';
import {DepartmentModel} from '../../../@core/models/department.model';
import {environment} from '../../../../environments/environment';
import {ContactTypeModel} from './contact-type.model';
import {ContactModel} from './contact.model';
import {Page} from "../../../@core/models/page.model";

@Injectable()
export class ContactsService {
    private readonly nonSecurityContactsUrl: string = `${environment.publicServerUrl}/non-security/contact`;
    private readonly getDepartmentsUrl: string = `${this.nonSecurityContactsUrl}/departments`;

    constructor(private http: HttpClient) {

    }

    getDepartments(): Observable<DepartmentModel[]> {
        return this.http.get<DepartmentModel[]>(this.getDepartmentsUrl);
    }

    getContactsByDepartment(page: number = 1, pageSize: number = 15, depId: number, contactType: ContactTypeModel)
    : Observable<Page<ContactModel>> {
        let params = new HttpParams();

        params = params.append('page', page.toString());
        params = params.append('pagesize', pageSize.toString());

        const url: string = `${this.nonSecurityContactsUrl}/${depId}/${contactType}`;

        return this.http.get<Page<ContactModel>>(url, {params});
    }

    getContactsBySearch(page: number = 1, pageSize: number = 15, search: string): Observable<Page<ContactModel>> {
        let params = new HttpParams();

        params = params.append('page', page.toString());
        params = params.append('pagesize', pageSize.toString());
        params = params.append('search', search);

        return this.http.get<Page<ContactModel>>(this.nonSecurityContactsUrl, {params});
    }
}

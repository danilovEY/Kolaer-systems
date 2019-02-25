import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs/Rx';
import {DepartmentModel} from '../models/department.model';
import {environment} from '../../../environments/environment';
import {ContactTypeModel} from '../models/contact-type.model';
import {ContactModel} from '../models/contact.model';
import {Page} from '../models/page.model';
import {map} from 'rxjs/internal/operators';
import {ContactRequestModel} from '../models/contact-request.model';

@Injectable()
export class ContactsService {
    private readonly contactsUrl: string = `${environment.publicServerUrl}/contacts`;
    private readonly getDepartmentsUrl: string = `${this.contactsUrl}/departments`;
    private readonly updateContactUrl: string = `${this.contactsUrl}/employees`;
    private readonly myContactUrl: string = `${environment.publicServerUrl}/user/contact`;

    constructor(private http: HttpClient) {

    }

    getDepartments(): Observable<DepartmentModel[]> {
        return this.http.get<DepartmentModel[]>(this.getDepartmentsUrl);
    }

    getContactsByDepartment(pageNum: number = 1, pageSize: number = 15, depId: number, contactType: ContactTypeModel)
    : Observable<Page<ContactModel>> {
        let params = new HttpParams();

        params = params.append('pageNum', pageNum.toString());
        params = params.append('pagesize', pageSize.toString());

        const url: string = `${this.getDepartmentsUrl}/${depId}/${contactType}`;

        return this.http.get<Page<ContactModel>>(url, {params});
    }

    getContactsBySearch(pageNum: number = 1, pageSize: number = 15, search: string): Observable<Page<ContactModel>> {
        let params = new HttpParams();

        params = params.append('pageNum', pageNum.toString());
        params = params.append('pagesize', pageSize.toString());
        params = params.append('search', search);

        return this.http.get<Page<ContactModel>>(this.contactsUrl, {params});
    }

    updateContact(employeeId: number, model: ContactRequestModel): Observable<ContactModel> {
        const url = `${this.updateContactUrl}/${employeeId}`;

        return this.http.put<ContactModel>(url, model)
            .pipe(map((request: ContactModel) => request));
    }

    getMyContacts(): Observable<ContactModel> {
        return this.http.get<ContactModel>(this.myContactUrl);
    }

    updateMyContacts(model: ContactRequestModel): Observable<ContactModel> {
        return this.http.put<ContactModel>(this.myContactUrl, model)
            .pipe(map((request: ContactModel) => request));
    }
}

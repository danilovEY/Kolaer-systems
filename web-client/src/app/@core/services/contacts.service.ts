import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs/Rx';
import {ContactTypeModel} from '../models/contact-type.model';
import {ContactModel} from '../models/contact.model';
import {Page} from '../models/page.model';
import {map} from 'rxjs/internal/operators';
import {ContactRequestModel} from '../models/contact-request.model';
import {RouterServiceConstant} from "../constants/router-service.constant";
import {Utils} from "../utils/utils";
import {PathVariableConstant} from "../constants/path-variable.constant";

@Injectable()
export class ContactsService {

    constructor(private http: HttpClient) {

    }

    getContactsByDepartment(pageNum: number = 1, pageSize: number = 15, depId: number, contactType: ContactTypeModel)
    : Observable<Page<ContactModel>> {
        let params = new HttpParams();

        params = params.append('pageNum', pageNum.toString());
        params = params.append('pagesize', pageSize.toString());

        const pathVariableMap: Map<string, string> = new Map()
            .set(PathVariableConstant.DEPARTMENT_ID, depId.toString())
            .set(PathVariableConstant.CONTACT_TYPE, ContactTypeModel[contactType]);

        const url: string = Utils.createUrlFromUrlTemplateMap(
            RouterServiceConstant.CONTACTS_DEPARTMENTS_ID_TYPE_URL,
            pathVariableMap
        );

        return this.http.get<Page<ContactModel>>(url, {params});
    }

    getContactsBySearch(pageNum: number = 1, pageSize: number = 15, search: string): Observable<Page<ContactModel>> {
        let params = new HttpParams();

        params = params.append('pageNum', pageNum.toString());
        params = params.append('pagesize', pageSize.toString());
        params = params.append('search', search);

        return this.http.get<Page<ContactModel>>(RouterServiceConstant.CONTACTS_URL, {params});
    }

    updateContact(employeeId: number, model: ContactRequestModel): Observable<ContactModel> {
        const url = Utils.createUrlFromUrlTemplate(
            RouterServiceConstant.CONTACTS_EMPLOYEES_ID_URL,
            PathVariableConstant.EMPLOYEE_ID,
            employeeId.toString()
        );

        return this.http.put<ContactModel>(url, model)
            .pipe(map((request: ContactModel) => request));
    }
}

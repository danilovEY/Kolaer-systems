import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs/Rx';
import {environment} from '../../../environments/environment';
import {ContactModel} from '../models/contact.model';
import {ContactRequestModel} from '../models/contact-request.model';
import {RouterServiceConstant} from "../constants/router-service.constant";

@Injectable()
export class UserService {
    private readonly USER_URL: string = `${environment.publicServerUrl}/user`;
    private readonly UPDATE_CONTACT_URL: string = `${this.USER_URL}/contact`;

    constructor(private http: HttpClient) {

    }

    updateMyContacts(model: ContactRequestModel): Observable<ContactModel> {
        return this.http.put<ContactModel>(this.UPDATE_CONTACT_URL, model);
    }

    getMyContacts(): Observable<ContactModel> {
        return this.http.get<ContactModel>(RouterServiceConstant.USER_CONTACT_URL);
    }
}

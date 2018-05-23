import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {RepositoryPasswordModel} from './repository-password.model';
import {Page} from '../../../@core/models/page.model';
import {environment} from '../../../../environments/environment';
import {Observable} from 'rxjs/Observable';
import {AuthenticationRestService} from '../../../@core/modules/auth/authentication-rest.service';
import {PasswordHistoryModel} from "./password-history.model";

@Injectable()
export class KolpassService {
    private readonly repositoryUrl: string = `${environment.publicServerUrl}/kolpass/rep`;
    private readonly getHistoryByRepositoryUrl: string = `/passwords`;
    private readonly getLastHistoryByRepositoryUrl: string = `${this.getHistoryByRepositoryUrl}/last`;

    constructor(private httpClient: HttpClient,
                private authService: AuthenticationRestService) {

    }

    getAllMyRepositories(page: number = 1, pageSize: number = 15): Observable<Page<RepositoryPasswordModel>> {
        if (!this.authService.authentication) {
            return Observable.empty();
        }


        let params = new HttpParams();

        params = params.append('page', page.toString());
        params = params.append('pagesize', pageSize.toString());
        
        return this.httpClient.get<Page<RepositoryPasswordModel>>(this.repositoryUrl, { params: params });
    }

    getHistoryInRepository(repId: number, page: number = 1, pageSize: number = 15): Observable<Page<PasswordHistoryModel>> {
        if (!this.authService.authentication) {
            return Observable.empty();
        }


        let params = new HttpParams();

        params = params.append('page', page.toString());
        params = params.append('pagesize', pageSize.toString());

        const url: string = `${this.repositoryUrl}/${repId}${this.getHistoryByRepositoryUrl}`;

        return this.httpClient.get<Page<PasswordHistoryModel>>(url, { params: params });
    }

    getLastHistoryByRepository(repId: number): Observable<PasswordHistoryModel> {
        if (!this.authService.authentication) {
            return Observable.empty();
        }

        const url: string = `${this.repositoryUrl}/${repId}${this.getLastHistoryByRepositoryUrl}`;

        return this.httpClient.get<PasswordHistoryModel>(url);
    }
}

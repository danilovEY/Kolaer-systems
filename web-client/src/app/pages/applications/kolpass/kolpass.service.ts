import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {RepositoryPasswordModel} from './repository-password.model';
import {Page} from '../../../@core/models/page.model';
import {environment} from '../../../../environments/environment';
import {EMPTY, Observable} from 'rxjs/index';
import {AuthenticationRestService} from '../../../@core/modules/auth/authentication-rest.service';
import {PasswordHistoryModel} from './password-history.model';
import {AccountModel} from '../../../@core/models/account.model';
import {RepositoryPasswordFilterModel} from './repository-password-filter.model';
import {RepositoryPasswordSortModel} from './repository-password-sort.model';
import {BaseService} from '../../../@core/services/base.service';

@Injectable()
export class KolpassService extends BaseService {
    private readonly repositoryUrl: string = `${environment.publicServerUrl}/kolpass/rep`;
    private readonly getHistoryByRepositoryUrl: string = `/passwords`;
    private readonly clearHistoryByRepositoryUrl: string = `/clear`;
    private readonly getLastHistoryByRepositoryUrl: string = `${this.getHistoryByRepositoryUrl}/last`;

    constructor(private httpClient: HttpClient,
                private authService: AuthenticationRestService) {
        super();
    }

    getAllMyRepositories(page: number = 1, pageSize: number = 15): Observable<Page<RepositoryPasswordModel>> {
        if (!this.authService.authentication) {
            return EMPTY;
        }


        let params = new HttpParams();

        params = params.append('page', page.toString());
        params = params.append('pagesize', pageSize.toString());
        
        return this.httpClient.get<Page<RepositoryPasswordModel>>(this.repositoryUrl, { params: params });
    }

    addRepository(newRepository: RepositoryPasswordModel): Observable<RepositoryPasswordModel>  {
        if (!this.authService.authentication) {
            return EMPTY;
        }

        const url: string = `${this.repositoryUrl}`;

        newRepository.id = null;
        newRepository.account = null;
        newRepository.urlImage = null;

        return this.httpClient.post<RepositoryPasswordModel>(url, newRepository);
    }

    deleteRepository(repId: number): Observable<any>  {
        if (!this.authService.authentication) {
            return EMPTY;
        }

        const url: string = `${this.repositoryUrl}/${repId}`;

        return this.httpClient.delete<any>(url);
    }

    editRepository(newRepository: RepositoryPasswordModel): Observable<RepositoryPasswordModel>  {
        if (!this.authService.authentication) {
            return EMPTY;
        }

        const url: string = `${this.repositoryUrl}/${newRepository.id}`;

        newRepository.account = null;

        return this.httpClient.put<RepositoryPasswordModel>(url, newRepository);
    }

    getRepository(repId: number): Observable<RepositoryPasswordModel>  {
        if (!this.authService.authentication) {
            return EMPTY;
        }

        const url: string = `${this.repositoryUrl}/${repId}`;

        return this.httpClient.get<RepositoryPasswordModel>(url);
    }

    getHistoryInRepository(repId: number, page: number = 1, pageSize: number = 15): Observable<Page<PasswordHistoryModel>> {
        if (!this.authService.authentication) {
            return EMPTY;
        }


        let params = new HttpParams();

        params = params.append('page', page.toString());
        params = params.append('pagesize', pageSize.toString());

        const url: string = `${this.repositoryUrl}/${repId}${this.getHistoryByRepositoryUrl}`;

        return this.httpClient.get<Page<PasswordHistoryModel>>(url, { params: params });
    }

    getLastHistoryByRepository(repId: number): Observable<PasswordHistoryModel> {
        if (!this.authService.authentication) {
            return EMPTY;
        }

        const url: string = `${this.repositoryUrl}/${repId}${this.getLastHistoryByRepositoryUrl}`;

        return this.httpClient.get<PasswordHistoryModel>(url);
    }

    removeHistoryFromRepository(repId: number, passwordId: number): Observable<any> {
        if (!this.authService.authentication) {
            return EMPTY;
        }

        const url: string = `${this.repositoryUrl}/${repId}${this.getHistoryByRepositoryUrl}/${passwordId}`;

        return this.httpClient.delete<any>(url);
    }

    clearHistoryFromRepository(repId: number): Observable<any> {
        if (!this.authService.authentication) {
            return EMPTY;
        }

        const url: string = `${this.repositoryUrl}/${repId}${this.clearHistoryByRepositoryUrl}`;

        return this.httpClient.delete<any>(url);
    }

    addPasswordToRepository(repId: number, newPassword: PasswordHistoryModel): Observable<PasswordHistoryModel>  {
        if (!this.authService.authentication) {
            return EMPTY;
        }

        const url: string = `${this.repositoryUrl}/${repId}${this.getHistoryByRepositoryUrl}`;

        newPassword.id = null;
        newPassword.passwordWriteDate = null;

        return this.httpClient.post<PasswordHistoryModel>(url, newPassword);
    }

    getSharedAccountsByRepId(repId: number, page: number = 1, pageSize: number = 15): Observable<AccountModel[]> {
        if (!this.authService.authentication) {
            return EMPTY;
        }


        let params = new HttpParams();

        params = params.append('page', page.toString());
        params = params.append('pagesize', pageSize.toString());

        const url: string = `${this.repositoryUrl}/${repId}/share`;

        return this.httpClient.get<AccountModel[]>(url, { params: params });
    }

    shareRepositoryToAccount(repositoryId: number, account: AccountModel): Observable<any> {
        const url: string = `${this.repositoryUrl}/${repositoryId}/share`;

        let params = new HttpParams();

        params = params.append('accountId', account.id.toString());

        return this.httpClient.post<any>(url, {}, { params: params });
    }

    deleteShareRepositoryToAccount(repositoryId: number, account: AccountModel): Observable<any> {
        const url: string = `${this.repositoryUrl}/${repositoryId}/share`;

        let params = new HttpParams();

        params = params.append('accountId', account.id.toString());

        return this.httpClient.delete<any>(url, { params: params });
    }

    getSharedRepositories(sort: RepositoryPasswordSortModel, filter: RepositoryPasswordFilterModel,
                          page: number, pageSize: number): Observable<Page<RepositoryPasswordModel>> {
        if (!this.authService.authentication) {
            return EMPTY;
        }


        let params = new HttpParams();

        params = params.append('page', page.toString()).append('pagesize', pageSize.toString());
        params = this.getSortAndFilterParam(params, sort, filter);

        const url: string = `${this.repositoryUrl}/share`;

        return this.httpClient.get<Page<RepositoryPasswordModel>>(url, { params: params });
    }
}

import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {RepositoryPasswordModel} from './repository-password.model';
import {Page} from '../../../@core/models/page.model';
import {environment} from '../../../../environments/environment';
import {Observable} from 'rxjs/Observable';
import {AuthenticationRestService} from '../../../@core/modules/auth/authentication-rest.service';

@Injectable()
export class KolpassService {
    private readonly getAllRepositories: string = `${environment.publicServerUrl}/kolpass/rep`;

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
        
        return this.httpClient.get<Page<RepositoryPasswordModel>>(this.getAllRepositories, { params: params });
    }
}

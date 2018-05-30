import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Page} from '../../../../@core/models/page.model';
import {Observable} from 'rxjs/Observable';
import {environment} from '../../../../../environments/environment';
import {BankAccountModel} from './bank-account.model';

@Injectable()
export class BankAccountService {
    private readonly bankAccountUrl = `${environment.publicServerUrl}/bank`;

    constructor(private http: HttpClient) {

    }

    getAllBankAccount(page: number, pageSize: number): Observable<Page<BankAccountModel>> {
        let params = new HttpParams();

        params = params.append('page', page.toString());
        params = params.append('pagesize', pageSize.toString());

        return this.http.get<Page<BankAccountModel>>(this.bankAccountUrl, {params: params});
    }

}

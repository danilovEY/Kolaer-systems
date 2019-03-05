import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Page} from '../../../../@core/models/page.model';
import {Observable} from 'rxjs/index';
import {environment} from '../../../../../environments/environment';
import {BankAccountModel} from './bank-account.model';
import {BankAccountRequestModel} from './bank-account-request.model';
import {BankAccountFilterModel} from './bank-account-filter.model';
import {BankAccountSortModel} from './bank-account-sort.model';
import {BaseService} from '../../../../@core/services/base.service';
import {EmployeeModel} from '../../../../@core/models/employee.model';

@Injectable()
export class BankAccountService extends BaseService {
    private readonly bankAccountUrl = `${environment.publicServerUrl}/bank`;

    constructor(private http: HttpClient) {
        super();
    }

    // TODO: refactoring
    findAllEmployeesWithAccount(query: string): Observable<Page<EmployeeModel>> {
        let params = new HttpParams();

        params = params.append('query', query);

        const url: string = `${this.bankAccountUrl}/employees`;

        return this.http.get<Page<EmployeeModel>>(url, {params: params});
    }

    getAllBankAccount(sort?: BankAccountSortModel, filter?: BankAccountFilterModel,
                      page: number = 1, pageSize: number = 15): Observable<Page<BankAccountModel>> {
        let params = new HttpParams();

        params = params.append('page', page.toString());
        params = params.append('pagesize', pageSize.toString());
        params = this.getSortAndFilterParam(params, sort, filter);

        return this.http.get<Page<BankAccountModel>>(this.bankAccountUrl, {params: params});
    }

    addBankAccount(bankAccount: BankAccountRequestModel): Observable<BankAccountModel> {
        return this.http.post<BankAccountModel>(this.bankAccountUrl, bankAccount);
    }

    editBankAccount(id: number, bankAccount: BankAccountRequestModel): Observable<BankAccountModel> {
        const url = `${this.bankAccountUrl}/${id}`;
        return this.http.put<BankAccountModel>(url, bankAccount);
    }

    deleteBankAccount(id: number): Observable<any> {
        const url = `${this.bankAccountUrl}/${id}`;
        return this.http.delete(url);
    }

}

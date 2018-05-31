import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Page} from '../../../../@core/models/page.model';
import {Observable} from 'rxjs/Observable';
import {environment} from '../../../../../environments/environment';
import {BankAccountModel} from './bank-account.model';
import {BankAccountRequestModel} from './bank-account-request.model';
import {SortModel} from '../../../../@core/models/sort.model';
import {BankAccountFilterModel} from './bank-account-filter.model';

@Injectable()
export class BankAccountService {
    private readonly bankAccountUrl = `${environment.publicServerUrl}/bank`;

    constructor(private http: HttpClient) {

    }

    getAllBankAccount(sort?: SortModel, filter?: BankAccountFilterModel,
                      page: number = 1, pageSize: number = 15): Observable<Page<BankAccountModel>> {
        let params = new HttpParams();

        params = params.append('page', page.toString());
        params = params.append('pagesize', pageSize.toString());

        if (sort) {
            params = sort.sortField ? params.append('sortField', sort.sortField) : params;
            params = sort.sortType ? params.append('sortType', sort.sortType.toString()) : params;
        }

        if (filter) {
            params = filter.id ? params.append('id', filter.id.toString()) : params;
            params = filter.check ? params.append('check', filter.check) : params;
            params = filter.employeeInitials ? params.append('employeeInitials', filter.employeeInitials) : params;
            params = filter.employeePost ? params.append('employeePost', filter.employeePost) : params;
            params = filter.employeeDepartment ? params.append('employeeDepartment', filter.employeeDepartment) : params;
        }

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

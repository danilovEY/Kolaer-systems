import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Page} from '../../../../@core/models/page.model';
import {Observable} from 'rxjs/Observable';
import {environment} from '../../../../../environments/environment';
import {BankAccountModel} from './bank-account.model';
import {BankAccountRequestModel} from './bank-account-request.model';
import {BankAccountFilterModel} from './bank-account-filter.model';
import {BankAccountSortModel} from './bank-account-sort.model';

@Injectable()
export class BankAccountService {
    private readonly bankAccountUrl = `${environment.publicServerUrl}/bank`;

    constructor(private http: HttpClient) {

    }

    getAllBankAccount(sort?: BankAccountSortModel, filter?: BankAccountFilterModel,
                      page: number = 1, pageSize: number = 15): Observable<Page<BankAccountModel>> {
        let params = new HttpParams();

        params = params.append('page', page.toString());
        params = params.append('pagesize', pageSize.toString());

        if (sort) {
            params = sort.sortId ? params.append('sortId', sort.sortId) : params;
            params = sort.sortCheck ? params.append('sortCheck', sort.sortCheck) : params;
            params = sort.sortEmployeeInitials ? params.append('sortEmployeeInitials', sort.sortEmployeeInitials) : params;
            params = sort.sortEmployeePost ? params.append('sortEmployeePost', sort.sortEmployeePost) : params;
            params = sort.sortEmployeeDepartment ? params.append('sortEmployeeDepartment', sort.sortEmployeeDepartment) : params;
        }

        if (filter) {
            params = filter.filterId ? params.append('filterId', filter.filterId.toString()) : params;
            params = filter.filterCheck ? params.append('filterCheck', filter.filterCheck) : params;
            params = filter.filterEmployeeInitials ? params.append('filterEmployeeInitials', filter.filterEmployeeInitials) : params;
            params = filter.filterEmployeePost ? params.append('filterEmployeePost', filter.filterEmployeePost) : params;
            params = filter.filterEmployeeDepartment ? params.append('filterEmployeeDepartment', filter.filterEmployeeDepartment) : params;
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

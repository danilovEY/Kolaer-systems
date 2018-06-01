import {CustomDataSource} from '../../../../@core/models/custom.data-source';
import {Page} from '../../../../@core/models/page.model';
import {BankAccountModel} from './bank-account.model';
import {BankAccountService} from './bank-account.service';
import {TableFilters} from '../../../../@theme/components/table/table-filter';
import {ColumnSort} from '../../../../@theme/components/table/column-sort';
import {BankAccountFilterModel} from './bank-account-filter.model';
import {Utils} from '../../../../@core/utils/utils';
import {BankAccountSortModel} from './bank-account-sort.model';

export class BankAccountDataSource extends CustomDataSource<BankAccountModel> {

    constructor(private bankAccountService: BankAccountService) {
        super();
    }

    loadElements(page: number, pageSize: number): Promise<BankAccountModel[]> {
        const tableFilters: TableFilters = this.getFilter();
        const columnSorts: ColumnSort[] = this.getSort();

        let bankAccountSort: BankAccountSortModel = null;
        let bankAccountFilter: BankAccountFilterModel = null;

        if (columnSorts.length > 0) {
            const columnSort: ColumnSort = columnSorts[0];
            bankAccountSort = new BankAccountSortModel();

            if (columnSort.field === 'employee.post.name') {
                bankAccountSort.sortEmployeePost = Utils.getSortType(columnSort.direction)
            } else if (columnSort.field === 'employee.department.name') {
                bankAccountSort.sortEmployeeDepartment = Utils.getSortType(columnSort.direction)
            } else if (columnSort.field === 'employee') {
                bankAccountSort.sortEmployeeInitials = Utils.getSortType(columnSort.direction)
            } else if (columnSort.field === 'check') {
                bankAccountSort.sortCheck = Utils.getSortType(columnSort.direction)
            } else {
                bankAccountSort.sortId = Utils.getSortType(columnSort.direction)
            }
        }

        if (tableFilters.filters.length > 0) {
            bankAccountFilter = new BankAccountFilterModel();
            for (const filer of tableFilters.filters) {
                if (filer.field === 'employee.post.name') {
                    bankAccountFilter.filterEmployeePost = filer.search;
                } else if (filer.field === 'employee.department.name') {
                    bankAccountFilter.filterEmployeeDepartment = filer.search;
                } else if (filer.field === 'employee') {
                    bankAccountFilter.filterEmployeeInitials = filer.search;
                } else if (filer.field === 'check') {
                    bankAccountFilter.filterCheck = filer.search;
                } else {
                    bankAccountFilter.filterId = Number(filer.search);
                }
            }
        }

        return this.bankAccountService.getAllBankAccount(bankAccountSort, bankAccountFilter,
            page, pageSize)
            .toPromise()
            .then((response: Page<BankAccountModel>) => this.setData(response));
    }
}

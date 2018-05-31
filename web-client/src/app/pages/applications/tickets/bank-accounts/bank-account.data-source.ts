import {CustomDataSource} from '../../../../@core/models/custom.data-source';
import {Page} from '../../../../@core/models/page.model';
import {BankAccountModel} from './bank-account.model';
import {BankAccountService} from './bank-account.service';
import {TableFilters} from '../../../../@theme/components/table/table-filter';
import {ColumnSort} from "../../../../@theme/components/table/column-sort";
import {SortModel} from "../../../../@core/models/sort.model";
import {BankAccountFilterModel} from "./bank-account-filter.model";

export class BankAccountDataSource extends CustomDataSource<BankAccountModel> {

    constructor(private bankAccountService: BankAccountService) {
        super();

        const columnSort: ColumnSort = new ColumnSort();
        columnSort.field = 'id';

        this.setSort([columnSort]);
    }

    loadElements(page: number, pageSize: number): Promise<BankAccountModel[]> {
        return this.getFilteredAndSorted();
    }

    getFilteredAndSorted(): Promise<any> {
        const tableFilters: TableFilters = this.getFilter();
        const columnSorts: ColumnSort[] = this.getSort();

        console.log(tableFilters);
        console.log(columnSorts);

        const bankAccountSort: SortModel = new SortModel(columnSorts[0].field, CustomDataSource.getSortType(columnSorts[0].direction));
        let bankAccountFilter: BankAccountFilterModel = null;

        if (tableFilters.filters.length > 0) {
            bankAccountFilter = new BankAccountFilterModel();
            for (const filer of tableFilters.filters) {
                if (filer.field === 'employee.post.name') {
                    bankAccountFilter.employeePost = filer.search;
                } if (filer.field === 'employee.department.name') {
                    bankAccountFilter.employeeDepartment = filer.search;
                } else if (filer.field === 'employee') {
                    bankAccountFilter.employeeInitials = filer.search;
                } else if (filer.field === 'check') {
                    bankAccountFilter.check = filer.search;
                } else {
                    bankAccountFilter.id = Number(filer.search);
                }
            }
        }

        return this.bankAccountService.getAllBankAccount(bankAccountSort, bankAccountFilter,
            this.getPage(), this.getPageSize())
            .toPromise()
            .then((response: Page<BankAccountModel>) => this.setData(response));
    }
}

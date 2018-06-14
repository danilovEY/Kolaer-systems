import {CustomDataSource} from '../../../../@core/models/custom.data-source';
import {Page} from '../../../../@core/models/page.model';
import {BankAccountModel} from './bank-account.model';
import {BankAccountService} from './bank-account.service';
import {BankAccountFilterModel} from './bank-account-filter.model';
import {BankAccountSortModel} from './bank-account-sort.model';

export class BankAccountDataSource extends CustomDataSource<BankAccountModel> {

    constructor(private bankAccountService: BankAccountService) {
        super();
    }

    loadElements(page: number, pageSize: number): Promise<BankAccountModel[]> {
        const bankAccountSort: BankAccountSortModel = this.getSortModel(new BankAccountSortModel());
        const bankAccountFilter: BankAccountFilterModel = this.getFilterModel(new BankAccountFilterModel());

        return this.bankAccountService.getAllBankAccount(bankAccountSort, bankAccountFilter,
            page, pageSize)
            .toPromise()
            .then((response: Page<BankAccountModel>) => this.setDataPage(response));
    }
}

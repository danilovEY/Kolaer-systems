import {CustomDataSource} from '../../../../@core/models/custom.data-source';
import {Page} from '../../../../@core/models/page.model';
import {BankAccountModel} from './bank-account.model';
import {BankAccountService} from './bank-account.service';
import {TableFilters} from '../../../../@theme/components/table/table-filter';
import {ColumnSort} from "../../../../@theme/components/table/column-sort";

export class BankAccountDataSource extends CustomDataSource<BankAccountModel> {

    constructor(private bankAccountService: BankAccountService) {
        super();
    }

    loadElements(page: number, pageSize: number): Promise<BankAccountModel[]> {
        return this.bankAccountService.getAllBankAccount(page, pageSize)
            .toPromise()
            .then((response: Page<BankAccountModel>) => this.setData(response));
    }


    protected sort(data: Array<any>): Array<any> {
        const sorts: ColumnSort[] = this.getSort();
        for (const sort of sorts) {
            const dir: number = CustomDataSource.getDirection(sort.direction);
            if (sort.field === 'post') {
               data = data.sort((a: BankAccountModel, b: BankAccountModel) => {
                   return CustomDataSource.COMPARE(dir,
                       a.employee.post.name,
                       b.employee.post.name);
               });
            } if (sort.field === 'department') {
                data = data.sort((a: BankAccountModel, b: BankAccountModel) => {
                    return CustomDataSource.COMPARE(dir,
                        a.employee.department.abbreviatedName,
                        b.employee.department.abbreviatedName);
                });
            }
        }
        return super.sort(data);
    }

    protected filter(data: Array<BankAccountModel>): Array<any> {
        const filter: TableFilters = this.getFilter();

        if (filter.filters.length > 0) {
            for (const filer of filter.filters) {
                if (filer.field === 'post') {
                    data = data.filter((bankAccountModel: BankAccountModel) => {
                        return CustomDataSource.FILTER(bankAccountModel.employee.post.name, filer.search);
                    });
                    filer.search = '';
                } if (filer.field === 'department') {
                    data = data.filter((bankAccountModel: BankAccountModel) => {
                        return CustomDataSource.FILTER(bankAccountModel.employee.department.abbreviatedName, filer.search);
                    });
                    filer.search = '';
                }
            }
        }

        return super.filter(data);
    }
}

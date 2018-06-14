import {KolpassService} from '../kolpass.service';
import {Page} from '../../../../@core/models/page.model';
import {CustomDataSource} from '../../../../@core/models/custom.data-source';
import {AccountModel} from '../../../../@core/models/account.model';

export class PasswordHistoryShareDataSource extends CustomDataSource<AccountModel> {

    constructor(private repId: number, private kolpassService: KolpassService) {
        super();
    }

    loadElements(page: number, pageSize: number): Promise<AccountModel[]> {
            return this.kolpassService.getSharedAccountsByRepId(this.repId, page, pageSize)
                .toPromise()
                .then((response: AccountModel[]) => this.setDataPage(new Page<AccountModel>(response)));
    }
}

import {KolpassService} from './kolpass.service';
import {Page} from '../../../@core/models/page.model';
import {PasswordHistoryModel} from './password-history.model';
import {CustomDataSource} from '../../../@core/models/custom.data-source';

export class PasswordHistoryDataSource extends CustomDataSource<PasswordHistoryModel> {

    constructor(private repId: number, private kolpassService: KolpassService) {
        super();
    }

    loadElements(page: number, pageSize: number): Promise<PasswordHistoryModel[]> {
        return this.kolpassService.getHistoryInRepository(this.repId, this.pagingConf['page'], this.pagingConf['perPage'])
            .toPromise()
            .then((response: Page<PasswordHistoryModel>) => this.setData(response));
    }
}

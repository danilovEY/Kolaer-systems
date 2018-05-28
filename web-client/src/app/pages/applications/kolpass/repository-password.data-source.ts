import {RepositoryPasswordModel} from './repository-password.model';
import {KolpassService} from './kolpass.service';
import {Page} from '../../../@core/models/page.model';
import {CustomDataSource} from '../../../@core/models/custom.data-source';

export class RepositoryPasswordDataSource extends CustomDataSource<RepositoryPasswordModel> {
    constructor(private kolpassService: KolpassService) {
        super();
    }

    loadElements(page: number, pageSize: number): Promise<RepositoryPasswordModel[]> {
        return this.kolpassService.getAllMyRepositories(this.pagingConf['page'], this.pagingConf['perPage'])
            .toPromise()
            .then((response: Page<RepositoryPasswordModel>) => this.setData(response));
    }
}

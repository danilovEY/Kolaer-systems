import {KolpassService} from '../kolpass.service';
import {Page} from '../../../../@core/models/page.model';
import {CustomDataSource} from '../../../../@core/models/custom.data-source';
import {RepositoryPasswordModel} from '../repository-password.model';
import {RepositoryPasswordFilterModel} from '../repository-password-filter.model';
import {RepositoryPasswordSortModel} from '../repository-password-sort.model';

export class RepositoryPasswordShareDataSource extends CustomDataSource<RepositoryPasswordModel> {

    constructor(private kolpassService: KolpassService) {
        super();
    }

    loadElements(page: number, pageSize: number): Promise<RepositoryPasswordModel[]> {
        const filter: RepositoryPasswordFilterModel = this.getFilterModel(new RepositoryPasswordFilterModel());
        const sort: RepositoryPasswordSortModel = this.getSortModel(new RepositoryPasswordSortModel());

        return this.kolpassService.getSharedRepositories(sort, filter, page, pageSize)
            .toPromise()
            .then((response: Page<RepositoryPasswordModel>) => this.setData(response));
    }
}

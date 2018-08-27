import {CustomDataSource} from '../../../../@core/models/custom.data-source';
import {TypeWorkModel} from '../../../../@core/models/type-work.model';
import {TypeWorkService} from '../../../../@core/services/type-work.service';
import {PageRequestModel} from '../../../../@core/models/page-request.model';
import {Page} from '../../../../@core/models/page.model';

export class TypeWorkDataSource extends CustomDataSource<TypeWorkModel> {

    constructor(private typeWorkService: TypeWorkService) {
        super();
    }

    loadElements(page: number, pageSize: number): Promise<TypeWorkModel[]> {
        const request: PageRequestModel = new PageRequestModel();
        request.number = page;
        request.pageSize = pageSize;

        return this.typeWorkService.getAll(request)
            .toPromise()
            .then((response: Page<TypeWorkModel>) => this.setDataPage(response));
    }

}

import {CustomDataSource} from '../../../../@core/models/custom.data-source';
import {TypeWorkModel} from '../../../../@core/models/type-work.model';
import {TypeWorkService} from '../../../../@core/services/type-work.service';
import {Page} from '../../../../@core/models/page.model';
import {FindTypeWorkRequest} from '../../../../@core/models/find-type-work-request';

export class TypeWorkDataSource extends CustomDataSource<TypeWorkModel> {

    constructor(private typeWorkService: TypeWorkService) {
        super();
    }

    loadElements(page: number, pageSize: number): Promise<TypeWorkModel[]> {
        const request: FindTypeWorkRequest = new FindTypeWorkRequest();
        request.number = page;
        request.pageSize = pageSize;

        return this.typeWorkService.getAll(request)
            .toPromise()
            .then((response: Page<TypeWorkModel>) => this.setDataPage(response));
    }

}

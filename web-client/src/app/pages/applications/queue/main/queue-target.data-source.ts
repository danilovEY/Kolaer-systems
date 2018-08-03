import {CustomDataSource} from '../../../../@core/models/custom.data-source';
import {QueueTargetModel} from '../../../../@core/models/queue-target.model';
import {QueueService} from '../queue.service';
import {Page} from '../../../../@core/models/page.model';

export class QueueTargetDataSource extends CustomDataSource<QueueTargetModel> {

    constructor(private queueService: QueueService) {
        super();
    }

    loadElements(page: number, pageSize: number): Promise<QueueTargetModel[]> {
        return this.queueService.getAllQueueTargets(page, pageSize)
            .toPromise()
            .then((response: Page<QueueTargetModel>) => this.setDataPage(response));
    }

}

import {CustomDataSource} from '../../../../@core/models/custom.data-source';
import {QueueService} from '../queue.service';
import {Page} from '../../../../@core/models/page.model';
import {QueueRequestModel} from '../../../../@core/models/queue-request.model';

export class QueueRequestDataSource extends CustomDataSource<QueueRequestModel> {

    constructor(private targetId: number, private queueService: QueueService) {
        super();
    }

    loadElements(page: number, pageSize: number): Promise<QueueRequestModel[]> {
        return this.queueService.getAllQueueRequest(this.targetId, page, pageSize)
            .toPromise()
            .then((response: Page<QueueRequestModel>) => this.setDataPage(response));
    }
}

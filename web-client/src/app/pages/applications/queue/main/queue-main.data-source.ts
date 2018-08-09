import {CustomDataSource} from '../../../../@core/models/custom.data-source';
import {QueueService} from '../queue.service';
import {Page} from '../../../../@core/models/page.model';
import {QueueScheduleModel} from '../../../../@core/models/queue-schedule.model';

export class QueueMainDataSource extends CustomDataSource<QueueScheduleModel> {

    constructor(private queueService: QueueService) {
        super();
    }

    loadElements(page: number, pageSize: number): Promise<QueueScheduleModel[]> {
        return this.queueService.getSchedulers(page, pageSize)
            .toPromise()
            .then((response: Page<QueueScheduleModel>) => this.setDataPage(response));
    }

}

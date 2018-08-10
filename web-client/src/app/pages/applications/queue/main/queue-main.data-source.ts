import {CustomDataSource} from '../../../../@core/models/custom.data-source';
import {QueueService} from '../queue.service';
import {Page} from '../../../../@core/models/page.model';
import {QueueScheduleModel} from '../../../../@core/models/queue-schedule.model';
import {QueueTargetFilterPageModel} from '../../../../@core/models/queue-filter-page.model';

export class QueueMainDataSource extends CustomDataSource<QueueScheduleModel> {

    constructor(private queueService: QueueService) {
        super();
    }

    loadElements(page: number, pageSize: number): Promise<QueueScheduleModel[]> {
        const filter: QueueTargetFilterPageModel = new QueueTargetFilterPageModel();
        filter.pageSize = pageSize;
        filter.number = page;

        return this.queueService.getSchedulers(filter)
            .toPromise()
            .then((response: Page<QueueScheduleModel>) => this.setDataPage(response));
    }

}

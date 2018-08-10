import {PageRequestModel} from './page-request.model';
import {QueueSortType} from './queue-sort-type';

export class QueueTargetFilterPageModel extends PageRequestModel {
    public sort: QueueSortType = QueueSortType.REQUEST_FROM_ASC;
}

import {QueueTargetModel} from './queue-target.model';
import {QueueRequestModel} from './queue-request.model';

export class QueueScheduleModel {
    constructor(public target: QueueTargetModel,
                public request: QueueRequestModel) {

    }
}

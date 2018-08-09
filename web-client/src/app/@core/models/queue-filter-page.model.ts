import {PageRequestModel} from './page-request.model';

export class QueueTargetFilterPageModel extends PageRequestModel {
    constructor(public filterTitle: string) {
        super();
    }
}

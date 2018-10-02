import {PageRequestModel} from './page-request.model';

export class FindTypeWorkRequest extends PageRequestModel {

    constructor(public searchName: string = '',
                public departmentIds: number[] = [],
                public onOnePage: boolean = false) {
        super();
    }

}

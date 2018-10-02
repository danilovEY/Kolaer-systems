import {PageRequestModel} from './page-request.model';

export class FindEmployeeRequestModel extends PageRequestModel {

    constructor(public query: string = '',
                public departmentIds: number[] = [],
                public onOnePage: boolean = false) {
        super();
    }

}

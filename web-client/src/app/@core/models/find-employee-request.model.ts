import {PageRequestModel} from './page-request.model';

export class FindEmployeeRequestModel extends PageRequestModel {

    public static findAll(): FindEmployeeRequestModel {
        return new FindEmployeeRequestModel();
    }

    constructor(public query: string = '',
                public departmentIds: number[] = [],
                public onOnePage: boolean = false) {
        super();
    }
}

import {PageRequestModel} from '../page-request.model';

export class FindDepartmentPageRequest extends PageRequestModel {
    constructor(public query: string = '', public onOnePage: boolean = false) {
        super();
    }
}

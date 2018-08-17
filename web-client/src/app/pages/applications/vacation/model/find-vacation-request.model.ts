import {PageRequestModel} from '../../../../@core/models/page-request.model';

export class FindVacationRequestModel extends PageRequestModel {
    public employeeId: number;
    public year: number;
}

import {PageRequestModel} from '../../page-request.model';
import {EmployeeSortTypeEnum} from "./employee-sort-type.enum";

export class FindEmployeeRequestModel extends PageRequestModel {

    public static findAll(): FindEmployeeRequestModel {
        const request = new FindEmployeeRequestModel();
        request.findByDeleted = false;

        return request;
    }

    constructor(
        public ids: number[] = [],
        public typeWorkIds: number[] = [],
        public postIds: number[] = [],
        public departmentIds: number[] = [],
        public findByPersonnelNumber: number = null,
        public findByAll: string = null,
        public findByInitials: string = null,
        public findByDepartmentName: string = null,
        public findByPostName: string = null,
        public findByDeleted: boolean = null,
        public sorts: EmployeeSortTypeEnum[] = [],
    ) {
        super();
    }
}

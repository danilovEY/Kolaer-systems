import {BaseModel} from "../base.model";

export class EmployeeCombinationModel extends BaseModel {
    employeeId: number;
    post: string;
    startDate: Date;
    endDate: Date;
}

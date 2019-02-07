import {BaseModel} from "../base.model";

export class EmploymentHistoryModel extends BaseModel {
    employeeId: number;
    organization: string;
    post: string;
    startWorkDate: Date;
    endWorkDate: Date;
}

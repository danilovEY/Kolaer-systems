import {BaseModel} from "../base.model";

export class EmployeeRelativeModel extends BaseModel {
    employeeId: number;
    initials: string;
    relationDegree: string;
    dateOfBirth: Date;
}

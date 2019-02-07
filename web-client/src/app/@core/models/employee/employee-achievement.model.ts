import {BaseModel} from "../base.model";

export class EmployeeAchievementModel extends BaseModel {
    employeeId: number;
    name: string;
    orderNumber: string;
    orderDate: Date;
}

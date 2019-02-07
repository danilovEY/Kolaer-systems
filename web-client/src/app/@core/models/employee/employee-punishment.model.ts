import {BaseModel} from "../base.model";

export class EmployeePunishmentModel extends BaseModel {
    employeeId: number;
    name: string;
    orderNumber: string;
    status: string;
    orderDate: Date;
    startPunishmentDate: Date;
}

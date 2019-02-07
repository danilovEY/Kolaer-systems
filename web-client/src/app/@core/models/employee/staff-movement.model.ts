import {BaseModel} from "../base.model";

export class StaffMovementModel extends BaseModel {
    employeeId: number;
    name: string;
    post: string;
    department: string;
    categoryUnit: string;
    salary: number;
    surchargeHarmfulness: number;
    cardSlam: string;
    classWorkingConditions: string;
    subClassWorkingConditions: string;
    orderNumber: string;
    startWorkDate: Date;
    endWorkDate: Date;
    orderDate: Date;
}

import {BaseModel} from "../base.model";

export class EmployeeEducationModel extends BaseModel {
    employeeId: number;
    typeEducation: string;
    institution: string;
    specialty: string;
    qualification: string;
    document: string;
    documentNumber: string;
    expirationDate: Date;
}

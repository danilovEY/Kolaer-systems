import {BaseModel} from "../base.model";

export class EmployeePersonalDocumentModel extends BaseModel {
    employeeId: number;
    name: string;
    issuedBy: string;
    documentNumber: string;
    dateOfIssue: Date;
}

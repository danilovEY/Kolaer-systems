import {BaseModel} from "../base.model";

export class EmployeePersonalDataModel extends BaseModel {
    employeeId: number;
    maritalStatus: string;
    phoneNumber: string;
    addressRegistration: string;
    addressResidential: string;
    placeOfBirth: string;
    disabilityGroup: string;
    disabilityDate: Date;
}

import {BaseModel} from "../../../../../../@core/models/base.model";

export class EmployeeMilitaryRegistrationModel extends BaseModel {
    public employeeId: number;
    public rank: string;
    public statusBy: string;
}

import {BaseModel} from "../../../../@core/models/base.model";
import {BusinessTripTypeEnum} from "./business-trip-type.enum";
import {BusinessTripEmployeeInfoModel} from "./business-trip-employee-info.model";
import {BusinessTripEmployeeModel} from "./business-trip-employee.model";

export class BusinessTripModel extends BaseModel {
    public businessTripType: BusinessTripTypeEnum;
    public organizationName: string;
    public documentNumber: string;
    public documentDate: Date;
    public comment: string;
    public writerEmployee: BusinessTripEmployeeInfoModel;
    public employees: BusinessTripEmployeeModel[];
}

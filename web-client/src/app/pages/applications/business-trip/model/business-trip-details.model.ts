import {BaseModel} from "../../../../@core/models/base.model";
import {BusinessTripTypeEnum} from "./business-trip-type.enum";
import {BusinessTripEmployeeInfoModel} from "./business-trip-employee-info.model";
import {BusinessTripEmployeeModel} from "./business-trip-employee.model";

export class BusinessTripDetailsModel extends BaseModel {
    public businessTripType: BusinessTripTypeEnum;
    public organizationName: string = 'Колатомэнергоремонт';
    public documentNumber: string;
    public documentDate: Date = new Date();
    public comment: string;
    public okpoCode: string;
    public reasonDescription: string;
    public reasonDocumentNumber: string;
    public reasonDocumentDate: Date = new Date();
    public writerEmployee: BusinessTripEmployeeInfoModel;
    public chiefEmployee: BusinessTripEmployeeInfoModel;
    public employees: BusinessTripEmployeeModel[];
}

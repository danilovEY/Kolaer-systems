import {BaseModel} from "../../../../@core/models/base.model";
import {BusinessTripTypeEnum} from "./business-trip-type.enum";

export class BusinessTripEditRequestModel extends BaseModel {
    public businessTripType: BusinessTripTypeEnum;
    public organizationName: string = 'Колатомэнергоремонт';
    public documentNumber: string;
    public documentDate: string;
    public comment: string;
    public okpoCode: string;
    public reasonDescription: string;
    public reasonDocumentNumber: string;
    public reasonDocumentDate: string;
    public chiefEmployeeId: number;
}

import {BusinessTripEmployeeInfoModel} from "./business-trip-employee-info.model";

export class BusinessTripEmployeeModel {
    public businessTripId: number;
    public employee: BusinessTripEmployeeInfoModel;
    public destinationCountry: string;
    public destinationCity: string;
    public destinationOrganizationName: string;
    public businessTripFrom: Date;
    public businessTripTo: Date;
    public businessTripDays: number;
    public targetDescription: string;
    public sourceOfFinancing: string;
}

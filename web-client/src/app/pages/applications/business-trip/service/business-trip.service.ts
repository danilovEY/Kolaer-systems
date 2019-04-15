import {Injectable} from "@angular/core";
import {BaseService} from "../../../../@core/services/base.service";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {BusinessTripModel} from "../model/business-trip.model";
import {RouterServiceConstant} from "../../../../@core/constants/router-service.constant";
import {Page} from "../../../../@core/models/page.model";
import {FindBusinessTripRequestModel} from "../model/find-business-trip-request.model";
import {BusinessTripDetailsModel} from "../model/business-trip-details.model";
import {Utils} from "../../../../@core/utils/utils";
import {PathVariableConstant} from "../../../../@core/constants/path-variable.constant";
import {BusinessTripEmployeeModel} from "../model/business-trip-employee.model";

@Injectable()
export class BusinessTripService extends BaseService {

    constructor(private httpClient: HttpClient) {
        super();
    }

    public findBusinessTrips(request: FindBusinessTripRequestModel): Observable<Page<BusinessTripModel>> {
        const url = RouterServiceConstant.BUSINESS_TRIP_URL;

        const params = this.includeHttpParams(new HttpParams(), request);

        return this.httpClient.get<Page<BusinessTripModel>>(url, {params: params});
    }

    public getBusinessTripById(businessTripId: number): Observable<BusinessTripDetailsModel> {
        const url = Utils.createUrlFromUrlTemplate(
            RouterServiceConstant.BUSINESS_TRIP_ID_URL,
            PathVariableConstant.BUSINESS_TRIP_ID,
            businessTripId.toString()
        );

        return this.httpClient.get<BusinessTripDetailsModel>(url);
    }

    getEmployeesByBusinessTripId(businessTripId: number): Observable<BusinessTripEmployeeModel[]> {
        const url = Utils.createUrlFromUrlTemplate(
            RouterServiceConstant.BUSINESS_TRIP_ID_EMPLOYEES_URL,
            PathVariableConstant.BUSINESS_TRIP_ID,
            businessTripId.toString()
        );

        return this.httpClient.get<BusinessTripEmployeeModel[]>(url);
    }
}

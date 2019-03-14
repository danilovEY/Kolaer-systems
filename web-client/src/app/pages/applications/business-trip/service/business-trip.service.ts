import {Injectable} from "@angular/core";
import {BaseService} from "../../../../@core/services/base.service";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {BusinessTripModel} from "../model/business-trip.model";
import {RouterServiceConstant} from "../../../../@core/constants/router-service.constant";
import {Page} from "../../../../@core/models/page.model";
import {FindBusinessTripRequestModel} from "../model/find-business-trip-request.model";

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

}

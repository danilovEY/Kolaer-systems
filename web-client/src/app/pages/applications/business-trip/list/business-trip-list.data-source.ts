import {BusinessTripModel} from "../model/business-trip.model";
import {BusinessTripService} from "../service/business-trip.service";
import {CustomDataSource} from "../../../../@core/models/custom.data-source";
import {Page} from "../../../../@core/models/page.model";
import {FindBusinessTripRequestModel} from "../model/find-business-trip-request.model";

export class BusinessTripListDataSource extends CustomDataSource<BusinessTripModel> {

    constructor(private businessTripService: BusinessTripService, private departmentId?: number) {
        super();
    }

    loadElements(page: number, pageSize: number): Promise<BusinessTripModel[]> {
        const request = new FindBusinessTripRequestModel();
        request.pageNum = page;
        request.pageSize = pageSize;

        // if (this.departmentId && this.departmentId > 0) {
        //     request.departmentIds = [this.departmentId];
        // }

        return this.businessTripService.findBusinessTrips(request)
            .toPromise()
            .then((response: Page<BusinessTripModel>) => this.setDataPage(response));
    }

}


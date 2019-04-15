import {BusinessTripService} from "../service/business-trip.service";
import {CustomDataSource} from "../../../../@core/models/custom.data-source";
import {BusinessTripEmployeeModel} from "../model/business-trip-employee.model";

export class BusinessTripDetailsEmployeeDataSource extends CustomDataSource<BusinessTripEmployeeModel> {

    constructor(private businessTripService: BusinessTripService, private businessTripId?: number) {
        super();
    }

    loadElements(page: number, pageSize: number): Promise<BusinessTripEmployeeModel[]> {
        return this.businessTripService.getEmployeesByBusinessTripId(this.businessTripId)
            .toPromise()
            .then((response: BusinessTripEmployeeModel[]) => this.setData(response));
    }

}


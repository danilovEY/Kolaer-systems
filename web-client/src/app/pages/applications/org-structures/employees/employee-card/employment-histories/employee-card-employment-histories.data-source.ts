import {CustomDataSource} from "../../../../../../@core/models/custom.data-source";
import {EmploymentHistoryModel} from "../../../../../../@core/models/employee/employment-history.model";
import {EmployeeEmploymentHistoriesService} from "./employee-employment-histories.service";
import {EmployeeCardService} from "../employee-card.service";

export class EmployeeCardEmploymentHistoriesDataSource extends CustomDataSource<EmploymentHistoryModel> {

    constructor(private employeeCardService: EmployeeCardService,
                private employeeEmploymentHistoriesService: EmployeeEmploymentHistoriesService) {
        super();
    }

    loadElements(page: number, pageSize: number): Promise<EmploymentHistoryModel[]> {
        return this.employeeEmploymentHistoriesService
            .getEmploymentHistoriesByEmployeeId(this.employeeCardService.selectedEmployeeId.getValue())
            .toPromise()
            .then((employmentHistories: EmploymentHistoryModel[]) => this.setData(employmentHistories));
    }

}

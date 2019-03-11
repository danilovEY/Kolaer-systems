import {CustomDataSource} from "../../../../../../@core/models/custom.data-source";
import {EmployeeRelativeModel} from "../../../../../../@core/models/employee/employee-relative.model";
import {EmployeeCardService} from "../employee-card.service";
import {EmployeeRelativeService} from "./employee-relative.service";

export class EmployeeCardRelativesDataSource extends CustomDataSource<EmployeeRelativeModel> {

    constructor(private employeeCardService: EmployeeCardService,
                private employeeRelativeService: EmployeeRelativeService) {
        super();
    }

    loadElements(page: number, pageSize: number): Promise<EmployeeRelativeModel[]> {
        return this.employeeRelativeService.getRelativesByEmployeeId(this.employeeCardService.selectedEmployeeId.getValue())
            .toPromise()
            .then((relatives: EmployeeRelativeModel[]) => this.setData(relatives));
    }

}

import {CustomDataSource} from "../../../../../../@core/models/custom.data-source";
import {EmployeeCombinationModel} from "../../../../../../@core/models/employee/employee-combination.model";
import {EmployeeCombinationsService} from "./employee-combinations.service";
import {EmployeeCardService} from "../employee-card.service";

export class EmployeeCardCombinationsDataSource extends CustomDataSource<EmployeeCombinationModel> {

    constructor(private employeeCardService: EmployeeCardService, private employeeCombinationService: EmployeeCombinationsService) {
        super();
    }

    loadElements(page: number, pageSize: number): Promise<EmployeeCombinationModel[]> {
        return this.employeeCombinationService.getCombinationsByEmployeeId(this.employeeCardService.selectedEmployeeId.getValue())
            .toPromise()
            .then((combinations: EmployeeCombinationModel[]) => this.setData(combinations))
    }

}

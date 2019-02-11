import {CustomDataSource} from "../../../../../../@core/models/custom.data-source";
import {EmployeeEducationModel} from "../../../../../../@core/models/employee/employee-education.model";
import {EmployeeEducationService} from "../../../../../../@core/services/employee-education.service";
import {EmployeeCardService} from "../employee-card.service";

export class EmployeeCardEducationDataSource extends CustomDataSource<EmployeeEducationModel> {

    constructor(private employeeEducationService: EmployeeEducationService,
                private employeeCardService: EmployeeCardService) {
        super();
    }

    loadElements(page: number, pageSize: number): Promise<EmployeeEducationModel[]> {
        return this.employeeEducationService.getAllEducationByEmployeeId(this.employeeCardService.selectedEmployeeId.getValue())
            .toPromise()
            .then((educations: EmployeeEducationModel[]) => this.setData(educations));
    }
}

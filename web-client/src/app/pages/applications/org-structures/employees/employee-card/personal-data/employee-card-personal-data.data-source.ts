import {CustomDataSource} from "../../../../../../@core/models/custom.data-source";
import {EmployeePersonalDataModel} from "../../../../../../@core/models/employee/employee-personal-data.model";
import {EmployeePersonalDataService} from "./employee-personal-data.service";
import {EmployeeCardService} from "../employee-card.service";

export class EmployeeCardPersonalDataDataSource extends CustomDataSource<EmployeePersonalDataModel> {

    constructor(private employeeCardService: EmployeeCardService,
                private employeePersonalDataService: EmployeePersonalDataService) {
        super();
    }

    loadElements(page: number, pageSize: number): Promise<EmployeePersonalDataModel[]> {
        return this.employeePersonalDataService
            .getPersonalDataByEmployeeId(this.employeeCardService.selectedEmployeeId.getValue())
            .toPromise()
            .then((personalData: EmployeePersonalDataModel[]) => this.setData(personalData));
    }

}

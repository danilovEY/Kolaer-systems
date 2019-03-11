import {CustomDataSource} from "../../../../../../@core/models/custom.data-source";
import {EmployeeStaffMovementModel} from "../../../../../../@core/models/employee/employee-staff-movement.model";
import {EmployeeCardService} from "../employee-card.service";
import {EmployeeStaffMovementsService} from "./employee-staff-movements.service";

export class EmployeeCardStaffMovementsDataSource extends CustomDataSource<EmployeeStaffMovementModel> {

    constructor(private employeeCardService: EmployeeCardService,
                private employeeStaffMovementsService: EmployeeStaffMovementsService) {
        super();
    }

    loadElements(page: number, pageSize: number): Promise<EmployeeStaffMovementModel[]> {
        return this.employeeStaffMovementsService
            .getStaffMovementsByEmployeeId(this.employeeCardService.selectedEmployeeId.getValue())
            .toPromise()
            .then((staffMovements: EmployeeStaffMovementModel[]) => this.setData(staffMovements));
    }

}

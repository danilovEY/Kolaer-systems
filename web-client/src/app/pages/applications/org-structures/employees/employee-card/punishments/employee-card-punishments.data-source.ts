import {CustomDataSource} from "../../../../../../@core/models/custom.data-source";
import {EmployeePunishmentModel} from "../../../../../../@core/models/employee/employee-punishment.model";
import {EmployeeCardService} from "../employee-card.service";
import {EmployeePunishmentService} from "./employee-punishment.service";

export class EmployeeCardPunishmentsDataSource extends CustomDataSource<EmployeePunishmentModel> {

    constructor(private employeeCardService: EmployeeCardService,
                private employeePunishmentService: EmployeePunishmentService) {
        super();
    }

    loadElements(page: number, pageSize: number): Promise<EmployeePunishmentModel[]> {
        return this.employeePunishmentService.getPunishmentsByEmployeeId(this.employeeCardService.selectedEmployeeId.getValue())
            .toPromise()
            .then((punishments: EmployeePunishmentModel[]) => this.setData(punishments));
    }

}

import {CustomDataSource} from "../../../../../../@core/models/custom.data-source";
import {EmployeePunishmentModel} from "../../../../../../@core/models/employee/employee-punishment.model";

export class EmployeeCardPunishmentsDataSource extends CustomDataSource<EmployeePunishmentModel> {
    private readonly fakeList: EmployeePunishmentModel[] = [];

    constructor() {
        super();

        const emp: EmployeePunishmentModel = new EmployeePunishmentModel();
        emp.name = 'Штраф в размере 5000 руб.';
        emp.status = 'Наложено';
        emp.orderNumber = '922152';
        emp.orderDate = new Date();
        emp.startPunishmentDate = new Date();

        this.fakeList.push(emp)
    }

    loadElements(page: number, pageSize: number): Promise<EmployeePunishmentModel[]> {
        return new Promise<EmployeePunishmentModel[]>(resolve => resolve(this.fakeList));
    }


}

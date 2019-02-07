import {CustomDataSource} from "../../../../../../@core/models/custom.data-source";
import {StaffMovementModel} from "../../../../../../@core/models/employee/staff-movement.model";

export class EmployeeCardStaffMovementsDataSource extends CustomDataSource<StaffMovementModel> {
    private readonly fakeList: StaffMovementModel[] = [];

    constructor() {
        super();

        const emp: StaffMovementModel = new StaffMovementModel();
        emp.name = 'Прием на работу';
        emp.post = 'Мастер 1 группы';
        emp.department = 'ЦРОС';
        emp.categoryUnit = 'Руководитель';
        emp.salary = 20152.00;
        emp.surchargeHarmfulness = 4;
        emp.cardSlam = 'СОУТ 99';
        emp.classWorkingConditions = 'Вредный';
        emp.subClassWorkingConditions = '3.1';
        emp.orderNumber = '156-лс';
        emp.orderDate = new Date();
        emp.startWorkDate = new Date();

        this.fakeList.push(emp)
    }

    loadElements(page: number, pageSize: number): Promise<StaffMovementModel[]> {
        return new Promise<StaffMovementModel[]>(resolve => resolve(this.fakeList));
    }


}

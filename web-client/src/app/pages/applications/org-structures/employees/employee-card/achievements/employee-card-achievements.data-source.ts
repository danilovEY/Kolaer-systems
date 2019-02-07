import {CustomDataSource} from "../../../../../../@core/models/custom.data-source";
import {EmployeeAchievementModel} from "../../../../../../@core/models/employee/employee-achievement.model";

export class EmployeeCardAchievementsDataSource extends CustomDataSource<EmployeeAchievementModel> {
    private readonly fakeList: EmployeeAchievementModel[] = [];

    constructor() {
        super();

        const emp: EmployeeAchievementModel = new EmployeeAchievementModel();
        emp.name = 'Благодарность от мэра';
        emp.orderNumber = '922152';
        emp.orderDate = new Date();

        this.fakeList.push(emp)
    }

    loadElements(page: number, pageSize: number): Promise<EmployeeAchievementModel[]> {
        return new Promise<EmployeeAchievementModel[]>(resolve => resolve(this.fakeList));
    }


}

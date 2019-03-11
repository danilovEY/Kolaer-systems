import {CustomDataSource} from "../../../../../../@core/models/custom.data-source";
import {EmployeeAchievementModel} from "../../../../../../@core/models/employee/employee-achievement.model";
import {EmployeeAchievementService} from "./employee-achievement.service";
import {EmployeeCardService} from "../employee-card.service";

export class EmployeeCardAchievementsDataSource extends CustomDataSource<EmployeeAchievementModel> {

    constructor(private employeeCardService: EmployeeCardService, private employeeAchievementService: EmployeeAchievementService) {
        super();
    }

    loadElements(page: number, pageSize: number): Promise<EmployeeAchievementModel[]> {
        return this.employeeAchievementService.getAchievementsByEmployeeId(this.employeeCardService.selectedEmployeeId.getValue())
            .toPromise()
            .then((achievements: EmployeeAchievementModel[]) => this.setData(achievements));
    }

}

import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {EmployeeAchievementModel} from "../../../../../../@core/models/employee/employee-achievement.model";
import {Utils} from "../../../../../../@core/utils/utils";
import {RouterServiceConstant} from "../../../../../../@core/constants/router-service.constant";
import {PathVariableConstant} from "../../../../../../@core/constants/path-variable.constant";

@Injectable()
export class EmployeeAchievementService {

    constructor(private httpClient: HttpClient) {

    }

    public getAchievementsByEmployeeId(employeeId: number): Observable<EmployeeAchievementModel[]> {
        const url = Utils.createUrlFromUrlTemplate(
            RouterServiceConstant.EMPLOYEES_ID_ACHIEVEMENTS_URL,
            PathVariableConstant.EMPLOYEE_ID,
            employeeId.toString()
        );

        return this.httpClient.get<EmployeeAchievementModel[]>(url);
    }

}

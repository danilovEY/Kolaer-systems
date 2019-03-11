import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Utils} from "../../../../../../@core/utils/utils";
import {RouterServiceConstant} from "../../../../../../@core/constants/router-service.constant";
import {PathVariableConstant} from "../../../../../../@core/constants/path-variable.constant";
import {Injectable} from "@angular/core";
import {EmploymentHistoryModel} from "../../../../../../@core/models/employee/employment-history.model";

@Injectable()
export class EmployeeEmploymentHistoriesService {

    constructor(private httpClient: HttpClient) {

    }

    public getEmploymentHistoriesByEmployeeId(employeeId: number): Observable<EmploymentHistoryModel[]> {
        const url = Utils.createUrlFromUrlTemplate(
            RouterServiceConstant.EMPLOYEES_ID_EMPLOYMENT_HISTORIES_URL,
            PathVariableConstant.EMPLOYEE_ID,
            employeeId.toString()
        );

        return this.httpClient.get<EmploymentHistoryModel[]>(url);
    }

}

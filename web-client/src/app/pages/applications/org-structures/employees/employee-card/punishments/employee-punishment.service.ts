import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Utils} from "../../../../../../@core/utils/utils";
import {RouterServiceConstant} from "../../../../../../@core/constants/router-service.constant";
import {PathVariableConstant} from "../../../../../../@core/constants/path-variable.constant";
import {Injectable} from "@angular/core";
import {EmployeePunishmentModel} from "../../../../../../@core/models/employee/employee-punishment.model";

@Injectable()
export class EmployeePunishmentService {

    constructor(private httpClient: HttpClient) {

    }

    public getPunishmentsByEmployeeId(employeeId: number): Observable<EmployeePunishmentModel[]> {
        const url = Utils.createUrlFromUrlTemplate(
            RouterServiceConstant.EMPLOYEES_ID_PUNISHMENTS_URL,
            PathVariableConstant.EMPLOYEE_ID,
            employeeId.toString()
        );

        return this.httpClient.get<EmployeePunishmentModel[]>(url);
    }

}

import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Utils} from "../../../../../../@core/utils/utils";
import {RouterServiceConstant} from "../../../../../../@core/constants/router-service.constant";
import {PathVariableConstant} from "../../../../../../@core/constants/path-variable.constant";
import {Injectable} from "@angular/core";
import {EmployeeStaffMovementModel} from "../../../../../../@core/models/employee/employee-staff-movement.model";

@Injectable()
export class EmployeeStaffMovementsService {

    constructor(private httpClient: HttpClient) {

    }

    public getStaffMovementsByEmployeeId(employeeId: number): Observable<EmployeeStaffMovementModel[]> {
        const url = Utils.createUrlFromUrlTemplate(
            RouterServiceConstant.EMPLOYEES_ID_STAFF_MOVEMENTS_URL,
            PathVariableConstant.EMPLOYEE_ID,
            employeeId.toString()
        );

        return this.httpClient.get<EmployeeStaffMovementModel[]>(url);
    }

}

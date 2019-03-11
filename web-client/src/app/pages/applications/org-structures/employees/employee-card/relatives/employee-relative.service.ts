import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Utils} from "../../../../../../@core/utils/utils";
import {RouterServiceConstant} from "../../../../../../@core/constants/router-service.constant";
import {PathVariableConstant} from "../../../../../../@core/constants/path-variable.constant";
import {Injectable} from "@angular/core";
import {EmployeeRelativeModel} from "../../../../../../@core/models/employee/employee-relative.model";

@Injectable()
export class EmployeeRelativeService {

    constructor(private httpClient: HttpClient) {

    }

    public getRelativesByEmployeeId(employeeId: number): Observable<EmployeeRelativeModel[]> {
        const url = Utils.createUrlFromUrlTemplate(
            RouterServiceConstant.EMPLOYEES_ID_RELATIVES_URL,
            PathVariableConstant.EMPLOYEE_ID,
            employeeId.toString()
        );

        return this.httpClient.get<EmployeeRelativeModel[]>(url);
    }

}

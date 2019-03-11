import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Utils} from "../../../../../../@core/utils/utils";
import {RouterServiceConstant} from "../../../../../../@core/constants/router-service.constant";
import {PathVariableConstant} from "../../../../../../@core/constants/path-variable.constant";
import {EmployeeCombinationModel} from "../../../../../../@core/models/employee/employee-combination.model";

@Injectable()
export class EmployeeCombinationsService {

    constructor(private httpClient: HttpClient) {

    }

    public getCombinationsByEmployeeId(employeeId: number): Observable<EmployeeCombinationModel[]> {
        const url = Utils.createUrlFromUrlTemplate(
            RouterServiceConstant.EMPLOYEES_ID_COMBINATIONS_URL,
            PathVariableConstant.EMPLOYEE_ID,
            employeeId.toString()
        );

        return this.httpClient.get<EmployeeCombinationModel[]>(url);
    }

}

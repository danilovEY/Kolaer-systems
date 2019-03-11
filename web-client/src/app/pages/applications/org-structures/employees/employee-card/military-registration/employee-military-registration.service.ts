import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {EmployeeMilitaryRegistrationModel} from "./employee-military-registration.model";
import {Utils} from "../../../../../../@core/utils/utils";
import {RouterServiceConstant} from "../../../../../../@core/constants/router-service.constant";
import {PathVariableConstant} from "../../../../../../@core/constants/path-variable.constant";

@Injectable()
export class EmployeeMilitaryRegistrationService {

    constructor(private httpClient: HttpClient) {

    }

    public getMilitaryRegistrationByEmployeeId(employeeId: number): Observable<EmployeeMilitaryRegistrationModel> {
        const url = Utils.createUrlFromUrlTemplate(
            RouterServiceConstant.EMPLOYEES_ID_MILITARY_REGISTRATION_URL,
            PathVariableConstant.EMPLOYEE_ID,
            employeeId.toString()
        );

        return this.httpClient.get<EmployeeMilitaryRegistrationModel>(url);
    }

}

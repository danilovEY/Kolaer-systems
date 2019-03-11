import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Utils} from "../../../../../../@core/utils/utils";
import {RouterServiceConstant} from "../../../../../../@core/constants/router-service.constant";
import {PathVariableConstant} from "../../../../../../@core/constants/path-variable.constant";
import {Injectable} from "@angular/core";
import {EmployeePersonalDataModel} from "../../../../../../@core/models/employee/employee-personal-data.model";

@Injectable()
export class EmployeePersonalDataService {

    constructor(private httpClient: HttpClient) {

    }

    public getPersonalDataByEmployeeId(employeeId: number): Observable<EmployeePersonalDataModel[]> {
        const url = Utils.createUrlFromUrlTemplate(
            RouterServiceConstant.EMPLOYEES_ID_PERSONAL_DATA_URL,
            PathVariableConstant.EMPLOYEE_ID,
            employeeId.toString()
        );

        return this.httpClient.get<EmployeePersonalDataModel[]>(url);
    }

}

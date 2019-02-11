import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {EmployeeEducationModel} from "../models/employee/employee-education.model";
import {Utils} from "../utils/utils";
import {RouterServiceConstant} from "../constants/router-service.constant";
import {PathVariableConstant} from "../constants/path-variable.constant";

@Injectable()
export class EmployeeEducationService {

    constructor(private httpClient: HttpClient) {

    }

    public getAllEducationByEmployeeId(employeeId: number): Observable<EmployeeEducationModel[]> {
        const url: string = Utils.createUrlFromUrlTemplate(
            RouterServiceConstant.EMPLOYEES_ID_EDUCATION_URL,
            PathVariableConstant.EMPLOYEE_ID,
            employeeId.toString()
        );

        return this.httpClient.get<EmployeeEducationModel[]>(url);
    }
}

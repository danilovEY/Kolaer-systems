import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Utils} from "../../../../../../@core/utils/utils";
import {RouterServiceConstant} from "../../../../../../@core/constants/router-service.constant";
import {PathVariableConstant} from "../../../../../../@core/constants/path-variable.constant";
import {EmployeePersonalDocumentModel} from "../../../../../../@core/models/employee/employee-personal-document.model";

@Injectable()
export class EmployeePersonalDocumentService {

    constructor(private httpClient: HttpClient) {

    }

    public getPersonalDocumentsByEmployeeId(employeeId: number): Observable<EmployeePersonalDocumentModel[]> {
        const url = Utils.createUrlFromUrlTemplate(
            RouterServiceConstant.EMPLOYEES_ID_PERSONAL_DOCUMENTS_URL,
            PathVariableConstant.EMPLOYEE_ID,
            employeeId.toString()
        );

        return this.httpClient.get<EmployeePersonalDocumentModel[]>(url);
    }

}

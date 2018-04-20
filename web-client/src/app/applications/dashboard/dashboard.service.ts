import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {EmployeeModel} from '../../commons/models/employee.model';
import {Observable} from 'rxjs/Observable';
import {environment} from '../../../environments/environment';
import 'rxjs/add/operator/map';
import {OtherEmployeeModel} from '../../commons/models/other-employee.model';
import {AuthenticationRestService} from '../../commons/modules/auth/authentication-rest.service';

@Injectable()
export class DashboardService {
    private readonly getAllEmployeeBithdayToday: string = `${environment.publicServerUrl}/employees/get/birthday/today`;
    private readonly getAllOtherEmployeeBithdayToday: string =
        `${environment.publicServerUrl}/organizations/employees/get/users/birthday/today`;

    constructor(private _authService: AuthenticationRestService,
                private _httpClient: HttpClient) {

    }

    getEmployeesBirthdayToday(): Observable<EmployeeModel[]> {
        return this._httpClient.get<EmployeeModel[]>(this.getAllEmployeeBithdayToday)
            .map((employees: EmployeeModel[]) => {
                for (const emp of employees) {
                    if (emp.birthday) {
                        emp.birthday = new Date(emp.birthday);
                    }

                    if (emp.dismissalDate) {
                        emp.dismissalDate = new Date(emp.dismissalDate);
                    }

                    if (emp.employmentDate) {
                        emp.employmentDate = new Date(emp.employmentDate);
                    }
                }
                return employees;
            });
    }

    getOtherEmployeesBirthdayToday(): Observable<OtherEmployeeModel[]> {
        return this._httpClient.get<OtherEmployeeModel[]>(this.getAllOtherEmployeeBithdayToday)
            .map((employees: OtherEmployeeModel[]) => {
                for (const emp of employees) {
                    if (emp.birthday) {
                        emp.birthday = new Date(emp.birthday);
                    }
                }
                return employees;
            });
    }
}

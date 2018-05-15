import {Component, OnInit} from '@angular/core';
import {EmployeeService} from '../../@core/services/employee.service';
import {EmployeeModel} from '../../@core/models/employee.model';
import {OtherEmployeeModel} from '../../@core/models/other-employee.model';
import {ServerExceptionModel} from '../../@core/models/server-exception.model';

@Component({
    selector: 'ngx-dashboard',
    styleUrls: ['./dashboard.component.scss'],
    templateUrl: './dashboard.component.html',
})
export class DashboardComponent implements OnInit {
    employeesBirthdayToday: EmployeeModel[] = [];
    otherEmployeesBirthdayToday: OtherEmployeeModel[] = [];
    loadingEmployeesBirthdayToday: boolean = true;

    today: Date = new Date();

    constructor(private employeeService: EmployeeService) {

    }

    ngOnInit() {
        this.employeeService.getEmployeesBirthdayToday()
            .finally(() => {
                this.loadingEmployeesBirthdayToday = false;
            })
            .subscribe(
                (employees: EmployeeModel[]) => this.employeesBirthdayToday = employees,
                (error: ServerExceptionModel) => console.log(error)
            );

        this.employeeService.getOtherEmployeesBirthdayToday()
            .subscribe(
                (employees: OtherEmployeeModel[]) => this.otherEmployeesBirthdayToday = employees,
                (error: ServerExceptionModel) => console.log(error)
            );
    }

    getShortNameOrganization(organization: string): string {
        return EmployeeService.getShortNameOrganization(organization);
    }
}

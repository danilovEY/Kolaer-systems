import {Component, OnDestroy, OnInit} from '@angular/core';
import {EmployeeMilitaryRegistrationModel} from "./employee-military-registration.model";
import {EmployeeMilitaryRegistrationService} from "./employee-military-registration.service";
import {EmployeeService} from "../../../../../../@core/services/employee.service";
import {Subject} from "rxjs";
import {map, switchMap, takeUntil} from "rxjs/operators";
import {EmployeeModel} from "../../../../../../@core/models/employee.model";

@Component({
    selector: 'employee-card-military-registration',
    templateUrl: './employee-card-military-registration.component.html',
    styleUrls: ['./employee-card-military-registration.component.scss']
})
export class EmployeeCardMilitaryRegistrationComponent implements OnInit, OnDestroy {
    private readonly destroySubjects: Subject<any> = new Subject<any>();

    currentMilitaryRegistration: EmployeeMilitaryRegistrationModel;

    constructor(private employeeMilitaryRegistrationService: EmployeeMilitaryRegistrationService,
                private employeeService: EmployeeService) {

    }

    ngOnInit(): void {
        this.employeeService.getCurrentEmployee()
            .pipe(
                map((currentEmployee: EmployeeModel) => currentEmployee.id),
                switchMap((currentEmployeeId: number) => this.employeeMilitaryRegistrationService
                    .getMilitaryRegistrationByEmployeeId(currentEmployeeId)),
                takeUntil(this.destroySubjects)
            ).subscribe((currentMilitaryRegistration: EmployeeMilitaryRegistrationModel) =>
            this.currentMilitaryRegistration = currentMilitaryRegistration);
    }

    ngOnDestroy() {
        this.destroySubjects.next(true);
        this.destroySubjects.complete();
    }
}

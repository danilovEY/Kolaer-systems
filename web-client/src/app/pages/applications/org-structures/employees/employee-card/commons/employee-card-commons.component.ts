import {Component, OnDestroy, OnInit} from '@angular/core';
import {EmployeeService} from "../../../../../../@core/services/employee.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Subject} from "rxjs";
import {EmployeeCardService} from "../employee-card.service";
import {switchMap, takeUntil} from "rxjs/operators";
import {EmployeeModel} from "../../../../../../@core/models/employee.model";
import {Category} from "../../../../../../@core/models/category.enum";

@Component({
    selector: 'employee-card-commons',
    templateUrl: './employee-card-commons.component.html',
    styleUrls: ['./employee-card-commons.component.scss']
})
export class EmployeeCardCommonsComponent implements OnInit, OnDestroy {
    private readonly ngUnsubscribe = new Subject();

    selectedEmployee: EmployeeModel;
    employeeDetailsLoading: boolean = true;

    constructor(private employeeService: EmployeeService,
                private employeeCardService: EmployeeCardService,
                private router: Router,
                private activeRoute: ActivatedRoute) {
    }

    ngOnInit(): void {
        this.employeeCardService.selectedEmployeeId
            .pipe(
                switchMap(employeeId => this.employeeService.getEmployeeById(employeeId)),
                takeUntil(this.ngUnsubscribe)
            )
            .subscribe(employee => {
                this.selectedEmployee = employee;
                this.employeeDetailsLoading = false;
            });
    }

    ngOnDestroy(): void {
        this.ngUnsubscribe.next(true);
        this.ngUnsubscribe.complete();
    }

    getCategoryName(category: Category): string {
        return Category[category];
    }
}

import {DefaultEditor} from 'ng2-smart-table';
import {Component, OnInit} from '@angular/core';
import {EmployeeService} from '../../../@core/services/employee.service';
import {EmployeeModel} from '../../../@core/models/employee.model';
import {Observable} from 'rxjs/Observable';
import {Subject} from 'rxjs/Subject';
import {catchError, debounceTime, distinctUntilChanged, switchMap, tap} from 'rxjs/operators'
import {concat} from 'rxjs/observable/concat';
import {of} from 'rxjs/observable/of';
import {Page} from '../../../@core/models/page.model';

@Component({
    selector: 'edit-employee',
    template: `
        <!--<select [ngClass]="inputClass"-->
                <!--class="form-control"-->
                <!--[(ngModel)]="cell.newValue"-->
                <!--[name]="cell.getId()"-->
                <!--[disabled]="!cell.isEditable()"-->
                <!--(click)="onClick.emit($event)"-->
                <!--(keydown.enter)="onEdited.emit($event)"-->
                <!--(keydown.esc)="onStopEditing.emit()">-->
            <!--<option *ngFor="let employee of employees" [value]="employee"-->
                    <!--[selected]="employee === cell.getValue()">{{ employee.initials }}-->
            <!--</option>-->
        <!--</select>-->

        <ng-select [items]="people3$ | async"
                   bindLabel="initials"
                   [loading]="people3Loading"
                   [typeahead]="people3input$"
                   [(ngModel)]="selectedPersons">
        </ng-select>
    `
})
export class EmployeeEditComponent extends DefaultEditor implements OnInit {
    people3$: Observable<EmployeeModel[]>;
    people3Loading = false;
    people3input$ = new Subject<string>();
    selectedPersons: EmployeeModel[] = <any>[{ initials: 'Karyn Wright' }, { initials: 'Other' }];
    // employees: EmployeeModel[] = [];

    constructor(private employeeService: EmployeeService) {
        super();
    }

    ngOnInit(): void {
        // this.employeeService.getAllEmployees(0, 0)
        //     .subscribe((response: Page<EmployeeModel>) => {
        //         this.employees = response.data;
        //     });

        this.people3$ = concat(
            of([]), // default items
            this.people3input$.pipe(
                debounceTime(200),
                distinctUntilChanged(),
                tap(() => this.people3Loading = true),
                switchMap(term => this.employeeService.getAllEmployees()
                    .map((request: Page<EmployeeModel>) => request.data)
                    .pipe(
                    catchError(() => of([])), // empty list on error
                    tap(() => this.people3Loading = false)
                ))
            )
        );
    }

}

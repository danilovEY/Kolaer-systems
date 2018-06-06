import {DefaultEditor} from 'ng2-smart-table';
import {Component, OnInit} from '@angular/core';
import {EmployeeService} from '../../../@core/services/employee.service';
import {EmployeeModel} from '../../../@core/models/employee.model';
import {Observable} from 'rxjs/Observable';
import {Subject} from 'rxjs/Subject';
import {catchError, debounceTime, distinctUntilChanged, switchMap, tap} from 'rxjs/operators'
import {of} from 'rxjs/observable/of';
import {Page} from '../../../@core/models/page.model';
import {SortTypeEnum} from '../../../@core/models/sort-type.enum';
import {EmployeeSortModel} from "../../../@core/models/employee-sort.model";
import {EmployeeFilterModel} from "../../../@core/models/employee-filter.model";

@Component({
    selector: 'edit-employee',
    styles: ['/deep/ ng-dropdown-panel { width: auto!important;}'],
    template: `        
        <div class="input-group" *ngIf="!cell.isEditable()">
            <input type="text" placeholder="Project" class="form-control" [ngModel]="cell.value" [disabled]="!cell.isEditable()"/>
        </div>
        
        <ng-select [ngClass]="inputClass" 
                   class="custom form-control"
                   *ngIf="cell.isEditable()"
                   [items]="people3$ | async"
                   bindLabel="initials"
                   appendTo="body"
                   (click)="onClick.emit($event)"
                   (keydown.enter)="onEdited.emit($event)"
                   (keydown.esc)="onStopEditing.emit()"
                   [loading]="people3Loading"
                   [typeahead]="people3input$"
                   [(ngModel)]="cell.newValue">
        </ng-select>
    `
})
export class EmployeeEditComponent extends DefaultEditor implements OnInit {
    people3$: Observable<EmployeeModel[]>;
    people3Loading = false;
    people3input$ = new Subject<string>();

    constructor(private employeeService: EmployeeService) {
        super();
    }


    ngOnInit(): void {
        this.people3$ = this.people3input$.pipe(
            debounceTime(1000),
            distinctUntilChanged(),
            tap(() => this.people3Loading = true),
            switchMap(term => this.employeeService
                .getAllEmployees(new EmployeeSortModel(null, SortTypeEnum.ASC), new EmployeeFilterModel(null, term), 0, 0)
                .map((request: Page<EmployeeModel>) => request.data)
                .pipe(
                catchError(() => of([])),
                tap(() => this.people3Loading = false)
            ))
        );
    }

}

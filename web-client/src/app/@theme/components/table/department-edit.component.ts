import {DefaultEditor} from 'ng2-smart-table';
import {Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {Subject} from 'rxjs/Subject';
import {catchError, debounceTime, distinctUntilChanged, switchMap, tap} from 'rxjs/operators'
import {of} from 'rxjs/observable/of';
import {Page} from '../../../@core/models/page.model';
import {SortTypeEnum} from '../../../@core/models/sort-type.enum';
import {DepartmentModel} from "../../../@core/models/department.model";
import {DepartmentSortModel} from "../../../@core/models/department-sort.model";
import {DepartmentFilterModel} from "../../../@core/models/department-filter.model";
import {DepartmentService} from "../../../@core/services/department.service";

@Component({
    selector: 'edit-department',
    styles: ['/deep/ ng-dropdown-panel { width: auto!important;}'],
    template: `        
        <div class="input-group" *ngIf="!cell.isEditable()">
            <input type="text" placeholder="Project" class="form-control" [ngModel]="cell.value" [disabled]="!cell.isEditable()"/>
        </div>
        
        <ng-select [ngClass]="inputClass" 
                   class="custom form-control"
                   *ngIf="cell.isEditable()"
                   [items]="people3$ | async"
                   bindLabel="abbreviatedName"
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
export class DepartmentEditComponent extends DefaultEditor implements OnInit {
    people3$: Observable<DepartmentModel[]>;
    people3Loading = false;
    people3input$ = new Subject<string>();

    constructor(private departmentService: DepartmentService) {
        super();
    }


    ngOnInit(): void {
        this.people3$ = this.people3input$.pipe(
            debounceTime(1000),
            distinctUntilChanged(),
            tap(() => this.people3Loading = true),
            switchMap(term => this.departmentService
                .getAllDepartments(new DepartmentSortModel(null, SortTypeEnum.ASC), new DepartmentFilterModel(null, term), 0, 0)
                .map((request: Page<DepartmentModel>) => request.data)
                .pipe(
                catchError(() => of([])),
                tap(() => this.people3Loading = false)
            ))
        );
    }

}

import {DefaultEditor} from 'ng2-smart-table';
import {Component, OnInit} from '@angular/core';
import {Observable, of, Subject} from 'rxjs/index';
import {catchError, debounceTime, distinctUntilChanged, switchMap, tap} from 'rxjs/operators'
import {Page} from '../../../@core/models/page.model';
import {DepartmentModel} from '../../../@core/models/department/department.model';
import {DepartmentService} from '../../../@core/services/department.service';
import {map} from 'rxjs/internal/operators';
import {FindDepartmentPageRequest} from "../../../@core/models/department/find-department-page-request";

@Component({
    selector: 'edit-department',
    styles: ['/deep/ ng-dropdown-panel { width: auto!important;}'],
    template: `        
        <div class="input-group" *ngIf="!cell.isEditable()">
            <input type="text" placeholder="Project" class="form-control" 
                   [ngModel]="cell.newValue.abbreviatedName" 
                   [disabled]="!cell.isEditable()"/>
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
                .find(FindDepartmentPageRequest.findAllRequest())
                .pipe(
                    map((request: Page<DepartmentModel>) => request.data),
                    catchError(() => of([])),
                    tap(() => this.people3Loading = false)
                ))
        );
    }

}

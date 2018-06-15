import {DefaultEditor} from 'ng2-smart-table';
import {Component, OnInit} from '@angular/core';
import {Observable, of, Subject} from 'rxjs/index';
import {catchError, debounceTime, distinctUntilChanged, map, switchMap, tap} from 'rxjs/operators'
import {BankAccountService} from '../bank-accounts/bank-account.service';
import {EmployeeModel} from '../../../../@core/models/employee.model';
import {EmployeeSortModel} from '../../../../@core/models/employee-sort.model';
import {EmployeeFilterModel} from '../../../../@core/models/employee-filter.model';
import {Page} from '../../../../@core/models/page.model';
import {SortTypeEnum} from '../../../../@core/models/sort-type.enum';

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
                   [loading]="people3Loading"
                   [typeahead]="people3input$"
                   [(ngModel)]="cell.newValue">
        </ng-select>
    `

})
export class EmployeeWithAccountEditComponent extends DefaultEditor implements OnInit {
    people3$: Observable<EmployeeModel[]>;
    people3Loading = false;
    people3input$ = new Subject<string>();

    constructor(private bankAccountService: BankAccountService) {
        super();
    }

    ngOnInit(): void {
        this.people3$ = this.people3input$.pipe(
            debounceTime(1000),
            distinctUntilChanged(),
            tap(() => this.people3Loading = true),
            switchMap(term => this.bankAccountService
                .getAllEmployeesWithAccount(new EmployeeSortModel(null, SortTypeEnum.ASC), new EmployeeFilterModel(null, `%${term}%`), 0, 0)
                .pipe(
                    map((request: Page<EmployeeModel>) => request.data),
                    catchError(() => of([])),
                    tap(() => this.people3Loading = false)
                ))
        );
    }

}

import {DefaultEditor} from 'ng2-smart-table';
import {Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {Subject} from 'rxjs/Subject';
import {catchError, debounceTime, distinctUntilChanged, switchMap, tap} from 'rxjs/operators'
import {of} from 'rxjs/observable/of';
import {Page} from '../../../@core/models/page.model';
import {AccountService} from "../../../@core/services/account.service";
import {AccountFilterModel} from "../../../@core/models/account-filter.model";
import {AccountModel} from "../../../@core/models/account.model";

@Component({
    selector: 'edit-account',
    styles: ['/deep/ ng-dropdown-panel { width: auto!important; z-index: 3000;}'],
    template: `        
        <div class="input-group" *ngIf="!cell.isEditable()">
            <input type="text" placeholder="Project" class="form-control" [ngModel]="cell.value" [disabled]="!cell.isEditable()"/>
        </div>
        
        <ng-select [ngClass]="inputClass" 
                   class="custom form-control"
                   *ngIf="cell.isEditable()"
                   [items]="people3$ | async"
                   bindLabel="label"
                   bindValue="account"
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
export class AccountEditComponent extends DefaultEditor implements OnInit {
    people3$: Observable<AccountView[]>;
    people3Loading = false;
    people3input$ = new Subject<string>();

    constructor(private accountService: AccountService) {
        super();
    }

    ngOnInit(): void {
        this.people3$ = this.people3input$.pipe(
            debounceTime(1000),
            distinctUntilChanged(),
            tap(() => this.people3Loading = true),
            switchMap(term => this.accountService
                .getAllAccounts(null, new AccountFilterModel(null, term), 0, 0)
                .map((request: Page<AccountModel>) => request.data)
                .map((accounts: AccountModel[]) =>
                    accounts.map((account: AccountModel) =>
                        new AccountView(account, account.employee ? account.employee.initials : account.username)))
                .pipe(
                catchError(() => of([])),
                tap(() => this.people3Loading = false)
            ))
        );
    }

}

export class AccountView {
    constructor(public account: AccountModel,
                public label: string = account.username) {

    }
}

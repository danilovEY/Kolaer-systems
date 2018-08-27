import {DefaultEditor} from 'ng2-smart-table';
import {Component, OnInit} from '@angular/core';
import {Observable, of, Subject} from 'rxjs/index';
import {catchError, debounceTime, distinctUntilChanged, switchMap, tap} from 'rxjs/operators'
import {Page} from '../../../@core/models/page.model';
import {map} from 'rxjs/internal/operators';
import {TypeWorkModel} from '../../../@core/models/type-work.model';
import {TypeWorkService} from '../../../@core/services/type-work.service';
import {FindTypeWorkRequest} from '../../../@core/models/find-type-work-request';

@Component({
    selector: 'edit-type-work',
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
                   bindLabel="name"
                   appendTo="body"
                   [clearable]="true"
                   (click)="onClick.emit($event)"
                   (keydown.enter)="onEdited.emit($event)"
                   (keydown.esc)="onStopEditing.emit()"
                   [loading]="people3Loading"
                   [typeahead]="people3input$"
                   [(ngModel)]="cell.newValue">
        </ng-select>
    `
})
export class TypeWorkEditComponent extends DefaultEditor implements OnInit {
    people3$: Observable<TypeWorkModel[]>;
    people3Loading = false;
    people3input$ = new Subject<string>();

    constructor(private typeWorkService: TypeWorkService) {
        super();
    }


    ngOnInit(): void {
        this.people3$ = this.people3input$.pipe(
            debounceTime(1000),
            distinctUntilChanged(),
            tap(() => this.people3Loading = true),
            switchMap(term => this.typeWorkService
                .getAll(new FindTypeWorkRequest(term))
                .pipe(
                    map((request: Page<TypeWorkModel>) => request.data),
                    catchError(() => of([])),
                    tap(() => this.people3Loading = false)
                ))
        );
    }

}

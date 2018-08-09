import {DefaultEditor} from 'ng2-smart-table';
import {Component, OnInit} from '@angular/core';
import {Observable, of, Subject} from 'rxjs/index';
import {catchError, debounceTime, distinctUntilChanged, switchMap, tap} from 'rxjs/operators'
import {Page} from '../../../@core/models/page.model';
import {map} from 'rxjs/internal/operators';
import {QueueTargetModel} from '../../../@core/models/queue-target.model';
import {QueueService} from '../../../pages/applications/queue/queue.service';

@Component({
    selector: 'edit-queue-target',
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
export class QueueTargetEditComponent extends DefaultEditor implements OnInit {
    people3$: Observable<QueueTargetModel[]>;
    people3Loading = false;
    people3input$ = new Subject<string>();

    constructor(private queueService: QueueService) {
        super();
    }


    ngOnInit(): void {
        this.people3$ = this.people3input$.pipe(
            debounceTime(1000),
            distinctUntilChanged(),
            tap(() => this.people3Loading = true),
            switchMap(term => this.queueService
                .getSchedulers(term)
                .pipe(
                    map((request: Page<QueueTargetModel>) => request.data),
                    catchError(() => of([])), 
                    tap(() => this.people3Loading = false)
                ))
        );
    }

}

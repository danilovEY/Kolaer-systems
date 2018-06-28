import {DefaultEditor} from 'ng2-smart-table';
import {Component, OnInit} from '@angular/core';
import {Observable, of, Subject} from 'rxjs/index';
import {catchError, debounceTime, distinctUntilChanged, switchMap, tap} from 'rxjs/operators'
import {Page} from '../../../@core/models/page.model';
import {SortTypeEnum} from '../../../@core/models/sort-type.enum';
import {map} from "rxjs/internal/operators";
import {PlacementModel} from "../../../@core/models/placement.model";
import {PlacementService} from "../../../@core/services/placement.service";
import {PlacementFilterModel} from "../../../@core/models/placement-filter.model";
import {PlacementSortModel} from "../../../@core/models/placement-sort.model";

@Component({
    selector: 'edit-placement',
    styles: ['/deep/ ng-dropdown-panel { width: auto!important;}'],
    template: `        
        <div class="input-group" *ngIf="!cell.isEditable()">
            <input type="text" placeholder="Project" class="form-control" 
                   [ngModel]="cell.newValue.name" 
                   [disabled]="!cell.isEditable()"/>
        </div>
        
        <ng-select [ngClass]="inputClass" 
                   class="custom form-control"
                   *ngIf="cell.isEditable()"
                   [items]="people3$ | async"
                   bindLabel="name"
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
export class PlacementEditComponent extends DefaultEditor implements OnInit {
    people3$: Observable<PlacementModel[]>;
    people3Loading = false;
    people3input$ = new Subject<string>();

    constructor(private placementService: PlacementService) {
        super();
    }

    ngOnInit(): void {
        this.people3$ = this.people3input$.pipe(
            debounceTime(1000),
            distinctUntilChanged(),
            tap(() => this.people3Loading = true),
            switchMap(term => this.placementService
                .getAllPlacements(new PlacementSortModel(null, SortTypeEnum.ASC), new PlacementFilterModel(null, term), 0, 0)
                .pipe(
                    map((request: Page<PlacementModel>) => request.data),
                    catchError(() => of([])),
                    tap(() => this.people3Loading = false)
                ))
        );
    }

}

import {DefaultEditor} from 'ng2-smart-table';
import {Component, OnInit} from '@angular/core';
import {Observable, of, Subject} from 'rxjs/index';
import {catchError, debounceTime, distinctUntilChanged, switchMap, tap} from 'rxjs/operators'
import {Page} from '../../../@core/models/page.model';
import {SortTypeEnum} from '../../../@core/models/sort-type.enum';
import {PostModel} from '../../../@core/models/post.model';
import {PostService} from '../../../@core/services/post.service';
import {PostSortModel} from '../../../@core/models/post-sort.model';
import {PostFilterModel} from '../../../@core/models/post-filter.model';
import {map} from 'rxjs/internal/operators';

@Component({
    selector: 'edit-post',
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
export class PostEditComponent extends DefaultEditor implements OnInit {
    people3$: Observable<PostModel[]>;
    people3Loading = false;
    people3input$ = new Subject<string>();

    constructor(private postService: PostService) {
        super();
    }


    ngOnInit(): void {
        this.people3$ = this.people3input$.pipe(
            debounceTime(1000),
            distinctUntilChanged(),
            tap(() => this.people3Loading = true),
            switchMap(term => this.postService
                .getAllPosts(new PostSortModel(null, SortTypeEnum.ASC), new PostFilterModel(null, term), 0, 0)
                .pipe(
                    map((request: Page<PostModel>) => request.data),
                    catchError(() => of([])),
                    tap(() => this.people3Loading = false)
                ))
        );
    }

}

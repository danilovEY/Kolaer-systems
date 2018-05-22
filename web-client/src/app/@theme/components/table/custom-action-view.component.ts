import {Component, EventEmitter, Input, OnInit} from '@angular/core';
import {ViewCell} from 'ng2-smart-table';
import {CustomActionModel} from './custom-action.model';
import {CustomActionEventModel} from "./custom-action-event.model";

@Component({
    selector: 'custom-action-view',
    template: `
        <div class="ng2-smart-actions" style="display: flex;">
            <ng-container *ngFor="let action of actions">
                <a *ngIf="beforeValueView(action, rowData)" href="#" [nbPopover]="action.description"
                   nbPopoverMode="hint"
                   class="ng2-smart-action"
                   style="margin: 0 5px;"
                   [innerHTML]="action.content"
                   (click)="onCustom(action, $event)"></a>
            </ng-container>
        </div>
  `,
})
export class CustomActionViewComponent implements ViewCell, OnInit {

    @Input() value: string | number;
    @Input() actions: CustomActionModel[] = [];
    @Input() rowData: any;

    custom = new EventEmitter<any>();
    actionBeforeValueView: Function;

    ngOnInit() {
    }

    onCustom(action: any, event: any) {
        event.preventDefault();
        event.stopPropagation();

        this.custom.emit(new CustomActionEventModel(action, this.rowData));
    }

    beforeValueView(action: CustomActionModel, data: any): boolean {
        return this.actionBeforeValueView ? this.actionBeforeValueView(new CustomActionEventModel(action, data)) : true;
    }
}

import {Component, EventEmitter, Input, Output} from '@angular/core';
import {ViewCell} from 'ng2-smart-table';
import {CustomActionModel} from './custom-action.model';

@Component({
    selector: 'custom-action-view',
    template: `
        <div class="ng2-smart-actions">
            <a *ngFor="let action of actions" href="#" [nbPopover]="action.description"
               nbPopoverMode="hint" nbPopoverPlacement="left"
               class="ng2-smart-action"
               [innerHTML]="action.content"
               (click)="onCustom(action, $event)"></a>
        </div>
  `,
})
export class CustomActionViewComponent implements ViewCell {
    @Input() value: string | number;
    @Input() actions: CustomActionModel[] = [];
    @Input() rowData: any;

    @Output() custom = new EventEmitter<any>();


    onCustom(action: any, event: any) {
        event.preventDefault();
        event.stopPropagation();

        this.custom.emit({
            action: action.name,
            data: this.rowData,
        });
    }
}

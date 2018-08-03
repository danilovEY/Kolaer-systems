import {Component} from '@angular/core';
import {DefaultEditor} from 'ng2-smart-table';
import {Grid} from 'ng2-smart-table/lib/grid';

@Component({
    selector: 'custom-action-edit',
    template: `
            <div class="ng2-smart-actions" style="display: flex; height: auto;">
                <a href="#" class="ng2-smart-action ng2-smart-action-edit-save" (click)="onSave($event)">
                    <i class="nb-checkmark"></i>
                </a>
                <a href="#" class="ng2-smart-action ng2-smart-action-edit-cancel" (click)="onCancelEdit($event)">
                    <i class="nb-close"></i>
                </a>
            </div>
    `,
})
export class CustomActionEditComponent extends DefaultEditor {

    onSave(event: any) {
        event.preventDefault();
        event.stopPropagation();

        this.onEdited.next(event);
    }

    onCancelEdit(event: any) {
        event.preventDefault();
        event.stopPropagation();

        const grid: Grid = this.cell.getColumn().editor.config.getGrid();
        if (grid && grid.createFormShown) {
            grid.createFormShown = false;

        } else {
            this.cell.getRow().isInEditing = false;
        }
    }
}

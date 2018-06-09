import {DefaultEditor} from 'ng2-smart-table';
import {Component, OnInit} from '@angular/core';
import {Utils} from '../../../@core/utils/utils';

@Component({
    selector: 'edit-date',
    template: `        
        <div class="input-group" style="margin-right: 35px;">
            <p-calendar [locale]="locale"
                        (onValueChange)="setValue($event)"
                        [ngModel]="currentDate" 
                        dateFormat="dd.mm.yy"
                        [disabled]="!cell.isEditable()"
                        (click)="onClick.emit($event)"
                        (keydown.enter)="onEdited.emit($event)"
                        (keydown.esc)="onStopEditing.emit()"
                        showIcon="true"></p-calendar>
        </div>
    `
})
export class DateEditComponent extends DefaultEditor implements OnInit {

    readonly locale = Utils.RU_LOCAL;
    currentDate: Date = new Date();

    ngOnInit(): void {
        this.cell.newValue = new Date(this.cell.newValue);
    }

    setValue(event: any) {
        this.currentDate = event;
        this.cell.setValue(Utils.getDateTimeToSend(event));
    }
}

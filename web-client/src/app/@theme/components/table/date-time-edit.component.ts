import {DefaultEditor} from 'ng2-smart-table';
import {Component, OnInit} from '@angular/core';
import {NgbDateStruct, NgbTimeStruct} from '@ng-bootstrap/ng-bootstrap';
import {Utils} from '../../../@core/utils/utils';

@Component({
    selector: 'edit-datetime',
    template: ` 
        <div class="center-block">        
            <div class="input-group">
                <input class="form-control" 
                       placeholder="Дата"
                       name="dp"
                       container="body"
                       type="text"
                       [disabled]="!cell.isEditable()"
                       (click)="onClick.emit($event)"
                       (keydown.enter)="onEdited.emit($event)"
                       (keydown.esc)="onStopEditing.emit()"
                       (dateSelect)="setValue($event)"
                       [startDate]="currentDate"
                       [(ngModel)]="currentDate"
                       ngbDatepicker #d="ngbDatepicker">
                <div class="input-group-append">
                    <button class="btn btn-outline-secondary" (click)="d.toggle()" type="button">
                        <i class="icon ion-md-calendar"></i>
                    </button>
                </div>
            </div>
        
            <div class="input-group">
                <ngb-timepicker [ngModel]="time" (ngModelChange)="setTimeValue($event)"></ngb-timepicker>
            </div>
        </div>
    `
})
export class DateTimeEditComponent extends DefaultEditor implements OnInit {
    currentDate: NgbDateStruct;
    time: NgbTimeStruct;

    ngOnInit(): void {
        const date: Date = new Date(this.cell.newValue) || new Date();

        this.currentDate = {
            year: date.getFullYear(),
            month: date.getMonth() + 1,
            day: date.getDate()
        };

        this.time = {
            hour: date.getHours() ? date.getHours() : 0,
            minute: date.getMinutes() ? date.getMinutes() : 0,
            second: 0
        };
    }

    setTimeValue(event: any) {
        this.time = event;
        this.setValue(event)
    }

    setValue(event: any) {
        const date: Date = new Date(this.currentDate.year,
            this.currentDate.month - 1,
            this.currentDate.day,
            this.time.hour,
            this.time.minute,
            this.time.second);

        this.cell.setValue(Utils.getDateTimeToSend(date));
    }
}


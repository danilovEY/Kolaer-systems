import {DefaultEditor} from 'ng2-smart-table';
import {Component, OnInit} from '@angular/core';
import {NgbDateStruct} from '@ng-bootstrap/ng-bootstrap';

@Component({
    selector: 'edit-date',
    template: `
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
    `
})
export class DateEditComponent extends DefaultEditor implements OnInit {
    currentDate: NgbDateStruct;

    ngOnInit(): void {
        const date: Date = this.cell.newValue || new Date();
        this.currentDate = {year: date.getFullYear(), month: date.getMonth() + 1, day: date.getDate()};
    }

    setValue(event: any) {
        this.cell.setValue(new Date(this.currentDate.year, this.currentDate.month - 1, this.currentDate.day));
    }
}


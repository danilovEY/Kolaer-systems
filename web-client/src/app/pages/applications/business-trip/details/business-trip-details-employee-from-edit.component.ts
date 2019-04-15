import {DefaultEditor} from 'ng2-smart-table';
import {Component, OnInit} from '@angular/core';
import {NgbDateStruct} from '@ng-bootstrap/ng-bootstrap';
import {BusinessTripCalculateModel} from "../model/business-trip-calculate.model";
import {Utils} from "../../../../@core/utils/utils";

@Component({
    selector: 'business-trip-details-employee-from-edit',
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
                   placement="top"
                   ngbDatepicker #d="ngbDatepicker">
            <div class="input-group-append">
                <button class="btn btn-outline-secondary" (click)="d.toggle()" type="button">
                    <i class="icon ion-md-calendar"></i>
                </button>
            </div>
        </div>
    `
})
export class BusinessTripDetailsEmployeeFromEditComponent extends DefaultEditor implements OnInit {
    currentDate: NgbDateStruct;

    constructor() {
        super();
    }

    ngOnInit(): void {
        let date: Date;

        if (this.cell.newValue) {
            if (this.cell.newValue instanceof Date) {
                date = this.cell.newValue
            } else {
                date = new Date(this.cell.newValue);
            }
        } else {
            date = new Date();
        }

        this.currentDate = {year: date.getFullYear(), month: date.getMonth() + 1, day: date.getDate()};
        this.cell.setValue(new Date(this.currentDate.year, this.currentDate.month - 1, this.currentDate.day));
    }

    setValue(event: any) {
        this.cell.setValue(new Date(this.currentDate.year, this.currentDate.month - 1, this.currentDate.day));

        const vacationTo = this.cell.getRow().cells[4].newValue;

        if (vacationTo) {
            const businessTripCalculate: BusinessTripCalculateModel = new BusinessTripCalculateModel();
            businessTripCalculate.from = this.cell.newValue;
            businessTripCalculate.to = vacationTo;
            businessTripCalculate.days = Utils.calculateInDaysBetweenDates(this.cell.newValue, vacationTo);

            this.cell.getRow().cells[5].getColumn().getConfig().setCalculateValue(businessTripCalculate);
        }
    }
}


import {DefaultEditor} from 'ng2-smart-table';
import {Component, OnInit} from '@angular/core';
import {NgbDateStruct} from '@ng-bootstrap/ng-bootstrap';
import {VacationService} from '../vacation.service';
import {VacationCalculateDaysRequestModel} from '../model/vacation-calculate-days-request.model';
import {Utils} from '../../../../@core/utils/utils';
import {VacationCalculateModel} from '../model/vacation-calculate.model';

@Component({
    selector: 'vacation-edit-from-date',
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
export class VacationDateToEditComponent extends DefaultEditor implements OnInit {
    currentDate: NgbDateStruct;

    constructor(private vacationService: VacationService) {
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
        this.setValue(this.currentDate);

        this.cell.getColumn().getConfig().setCalculateValue = (value) => this.setCalculateValue(value);
    }

    setCalculateValue(event: VacationCalculateModel) {
        const date = new Date(event.to);
        this.currentDate = {year: date.getFullYear(), month: date.getMonth() + 1, day: date.getDate()};
        this.cell.setValue(date);
    }

    setValue(event: any) {
        this.cell.setValue(new Date(this.currentDate.year, this.currentDate.month - 1, this.currentDate.day));

        const vacationFrom = this.cell.getRow().cells[0].newValue;

        if (vacationFrom) {
            const request = new VacationCalculateDaysRequestModel();
            request.from = Utils.getDateTimeToSend(vacationFrom);
            request.to = Utils.getDateTimeToSend(this.cell.newValue);

            this.vacationService.calculateDays(request)
                .subscribe(vacCalc => {
                    this.cell.getRow().cells[2].getColumn().getConfig().setCalculateValue(vacCalc);
                });
        }
    }
}


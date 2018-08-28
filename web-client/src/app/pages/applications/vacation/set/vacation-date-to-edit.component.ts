import {DefaultEditor} from 'ng2-smart-table';
import {Component, OnInit} from '@angular/core';
import {NgbDateStruct} from '@ng-bootstrap/ng-bootstrap';
import {VacationService} from '../vacation.service';
import {VacationCalculateDaysRequestModel} from '../model/vacation-calculate-days-request.model';
import {Utils} from '../../../../@core/utils/utils';
import {VacationCalculateModel} from '../model/vacation-calculate.model';
import {VacationPeriodService} from '../vacation-period.service';
import {VacationPeriodModel} from '../model/vacation-period.model';

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
export class VacationDateToEditComponent extends DefaultEditor implements OnInit {
    currentDate: NgbDateStruct;

    constructor(private vacationService: VacationService,
                private vacationPeriodService: VacationPeriodService) {
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

        const period: VacationPeriodModel = this.vacationPeriodService.getSelectedPeriod();
        if (period) {
            this.setYear(period.year);
        }

        this.cell.getColumn().getConfig().setCalculateValue = (value) => this.setCalculateValue(value);

        this.vacationPeriodService
            .selectedPeriodEvent
            .subscribe(p => this.setYear(p.year));
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
            request.from = Utils.getDateToSend(vacationFrom);
            request.to = Utils.getDateToSend(this.cell.newValue);

            this.vacationService.calculateDays(request)
                .subscribe(vacCalc => {
                    this.cell.getRow().cells[2].getColumn().getConfig().setCalculateValue(vacCalc);
                });
        }
    }

    private setYear(year: number) {
        const date = this.cell.newValue;
        if (date.getFullYear() !== year) {
            this.currentDate = {year: year, month: 1, day: 1};
            this.cell.setValue(new Date(this.currentDate.year, this.currentDate.month - 1, this.currentDate.day));
        }
    }
}


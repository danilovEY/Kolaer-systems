import {DefaultEditor} from 'ng2-smart-table';
import {Component, OnInit} from '@angular/core';
import {VacationService} from '../vacation.service';
import {VacationCalculateModel} from '../model/vacation-calculate.model';
import {Utils} from '../../../../@core/utils/utils';
import {VacationCalculateDateRequestModel} from '../model/vacation-calculate-date-request.model';

@Component({
    selector: 'vacation-days-edit',
    template: `
        <label for="daysInput" *ngIf="vacationCalculate && vacationCalculate.holidayDays > 0">
            Праздничных дней: {{ vacationCalculate.holidayDays }}
        </label>
        <div class="input-group">
            <input [ngClass]="inputClass"
                   class="form-control"
                   [ngModel]="cell.newValue"
                   (ngModelChange)="setValue($event)"
                   [name]="cell.getId()"
                   [placeholder]="cell.getTitle()"
                   [disabled]="!cell.isEditable()"
                   (click)="onClick.emit($event)"
                   (keydown.enter)="onEdited.emit($event)"
                   (keydown.esc)="onStopEditing.emit()">
        </div>
    `
})
export class VacationDaysEditComponent extends DefaultEditor implements OnInit {
    vacationCalculate: VacationCalculateModel;

    constructor(private vacationService: VacationService) {
        super();
    }

    ngOnInit(): void {
        this.cell.setValue(1);
        this.cell.getColumn().getConfig().setCalculateValue = (value) => this.setCalculateValue(value);
    }

    setCalculateValue(event: VacationCalculateModel) {
        this.vacationCalculate = event;
        this.cell.setValue(event.days);
    }

    setValue(event: number) {
        this.cell.setValue(event);

        const vacationFrom = this.cell.getRow().cells[0].newValue;

        if (vacationFrom) {
            const request = new VacationCalculateDateRequestModel();
            request.from = Utils.getDateTimeToSend(vacationFrom);
            request.days = event;

            this.vacationService.calculateDate(request)
                .subscribe(vacCalc => {
                    this.vacationCalculate = vacCalc;
                    this.cell.getRow().cells[1].getColumn().getConfig().setCalculateValue(vacCalc);
                });
        }
    }

}


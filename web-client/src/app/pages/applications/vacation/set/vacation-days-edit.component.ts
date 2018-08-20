import {DefaultEditor} from 'ng2-smart-table';
import {Component, OnInit} from '@angular/core';
import {VacationService} from '../vacation.service';
import {VacationCalculateModel} from '../model/vacation-calculate.model';

@Component({
    selector: 'vacation-days-edit',
    template: `
        <label for="daysInput" *ngIf="vacationCalculate && vacationCalculate.holidayDays > 0">
            Праздничных дней: {{ vacationCalculate.holidayDays }}
        </label>
        <div class="input-group">
            <input [ngClass]="inputClass"
                   class="form-control"
                   [(ngModel)]="cell.newValue"
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
        this.cell.getColumn().getConfig().setCalculateValue = (value) => this.setValue(value);
    }

    setValue(event: VacationCalculateModel) {
        this.vacationCalculate = event;
        this.cell.setValue(event.days);
    }

}


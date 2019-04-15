import {DefaultEditor} from 'ng2-smart-table';
import {Component, OnInit} from '@angular/core';
import {BusinessTripCalculateModel} from "../model/business-trip-calculate.model";
import {Utils} from "../../../../@core/utils/utils";

@Component({
    selector: 'vacation-days-edit',
    template: `        
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
export class BusinessTripDetailsEmployeeDaysEditComponent extends DefaultEditor implements OnInit {
    businessTripCalculate: BusinessTripCalculateModel;

    constructor() {
        super();
    }

    ngOnInit(): void {
        this.cell.setValue(this.cell.newValue);
        this.cell.getColumn().getConfig().setCalculateValue = (value) => this.setCalculateValue(value);
    }

    setCalculateValue(event: BusinessTripCalculateModel) {
        this.businessTripCalculate = event;
        this.cell.setValue(event.days);
    }

    setValue(event: number) {
        this.cell.setValue(event);

        const vacationFrom = this.cell.getRow().cells[3].newValue;

        if (vacationFrom) {
            this.businessTripCalculate = new BusinessTripCalculateModel();
            this.businessTripCalculate.from = vacationFrom;
            this.businessTripCalculate.days = event;
            this.businessTripCalculate.to = Utils.calculateDateBetweenDateAndDays(vacationFrom, event);

            this.cell.getRow().cells[4].getColumn().getConfig().setCalculateValue(this.businessTripCalculate);
        }
    }

}


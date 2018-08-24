import {EventEmitter, Injectable} from '@angular/core';
import {VacationPeriodModel} from './model/vacation-period.model';

@Injectable()
export class VacationPeriodService {
    private selectedPeriod: VacationPeriodModel;

    public selectedPeriodEvent = new EventEmitter<VacationPeriodModel>();

    constructor() {
        this.selectedPeriodEvent.subscribe(period => this.selectedPeriod = period);
    }

    getSelectedPeriod(): VacationPeriodModel {
        return this.selectedPeriod;
    }
}

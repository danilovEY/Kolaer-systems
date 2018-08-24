import {Component, OnInit} from '@angular/core';
import {Utils} from '../../../../@core/utils/utils';
import {ProductionCalendarService} from '../../../../@core/services/production-calendar.service';
import {HolidayModel} from '../../../../@core/models/holiday.model';
import {NgbDateStruct} from '@ng-bootstrap/ng-bootstrap';
import {TypeDay} from '../../../../@core/models/typeday.enum';
import {finalize} from 'rxjs/internal/operators';

@Component({
    selector: 'production-calendar-main',
    styleUrls: ['./production-calendar-main.scss'],
    templateUrl: './production-calendar-main.html'
})
export class ProductionCalendarMainComponent implements OnInit {
    private holidays: HolidayModel[] = [];

    months: NgbDateStruct[] = [];
    holidaysLoading: boolean = true;

    years: any[] = [];
    selectedYear: any;

    constructor(private productionCalendarService: ProductionCalendarService) {
        const currentYear = new Date().getFullYear();

        this.years.push(
            { year: currentYear + 1 },
            { year: currentYear },
            { year: currentYear - 1 }
        );

        this.selectedYear = this.years[1];
    }

    ngOnInit() {
        this.selectYear(this.selectedYear);
    }

    selectYear(event: number) {
        this.selectedYear = event;

        this.holidaysLoading = true;

        this.months = [];
        this.holidays = [];

        for (let index = 1; index < 13; index++) {
            this.months.push({year: this.selectedYear.year, month: index, day: 1});
        }

        this.productionCalendarService.getAllHolidayByYear(this.selectedYear.year)
            .pipe(finalize(() => this.holidaysLoading = false))
            .subscribe((response: HolidayModel[]) => this.holidays = response);
    }

    isWeekend(date: NgbDateStruct) {
        const d = new Date(date.year, date.month - 1, date.day);
        return d.getDay() === 0 || d.getDay() === 6;
    }

    isDisabled(date: NgbDateStruct, current: {month: number}) {
        return date.month !== current.month;
    }

    isHoliday(month: number, date: NgbDateStruct): boolean {
        if (date['holiday']) {
            return true;
        }

        if (date.month !== month) {
            return false;
        }

        for (const holiday of this.holidays) {
            if (holiday.holidayDate.getDate() === date.day &&
                holiday.holidayDate.getMonth() + 1 === date.month &&
                holiday.holidayDate.getFullYear() === date.year) {

                date['holiday'] = holiday;

                return true;
            }
        }

        return false;
    }

    isOutput(month: number, date: NgbDateStruct): boolean {
        if (date.month !== month) {
            return false;
        }

        const holiday: HolidayModel = date['holiday'];

        return holiday && holiday.holidayType === Utils.keyFromValue(TypeDay, TypeDay.HOLIDAY);
    }

    dateToPopup(date: NgbDateStruct): string {
        const holiday: HolidayModel = date['holiday'];
        return holiday ? `${holiday.name} (${TypeDay[holiday.holidayType]})` : date.day.toString();
    }
}

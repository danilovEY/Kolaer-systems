import {NgbDatepickerI18n, NgbDateStruct} from '@ng-bootstrap/ng-bootstrap';
import {Utils} from '../utils/utils';

export class CustomDatePickerLangService extends NgbDatepickerI18n {

    getWeekdayShortName(weekday: number): string {
        return Utils.RU_LOCAL.dayNamesMin[weekday - 1];
    }
    getMonthShortName(month: number): string {
        return Utils.RU_LOCAL.monthNamesShort[month - 1];
    }
    getMonthFullName(month: number): string {
        return this.getMonthShortName(month);
    }

    getDayAriaLabel(date: NgbDateStruct): string {
        return `${date.day}-${date.month}-${date.year}`;
    }
}

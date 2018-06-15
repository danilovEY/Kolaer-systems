import {SortTypeEnum} from '../models/sort-type.enum';
import {DatePipe} from '@angular/common';

export class Utils {
    private static datePipe = new DatePipe('en-US');

    public static readonly RU_LOCAL = {
        firstDayOfWeek: 1,
        dayNames: ['Воскресенье', 'Понедельник', 'Вторник', 'Среда', 'Четверг', 'Пятница', 'Суббота'],
        dayNamesShort: ['Вс', 'Пн', 'Вт', 'Ср', 'Чт', 'Пт', 'Сб'],
        dayNamesMin: ['Пн', 'Вт', 'Ср', 'Чт', 'Пт', 'Сб', 'Вс'],
        monthNames: [ 'Январь', 'Февраль', 'Март', 'Апрель', 'Май', 'Июнь', 'Июль', 'Август', 'Сентябрь', 'Октябрь', 'Ноябрь', 'Декабрь' ],
        monthNamesShort: [ 'Янв', 'Фев', 'Мар', 'Апр', 'Май', 'Июн', 'Июл', 'Авг', 'Сен', 'Окт', 'Ноя', 'Дек' ],
        today: 'Сегодня',
        clear: 'Очистить'
    };

    public static copyToChipboard(content: string = '') {
        const selBox = document.createElement('textarea');
        selBox.style.position = 'fixed';
        selBox.style.left = '0';
        selBox.style.top = '0';
        selBox.style.opacity = '0';
        selBox.value = content;
        document.body.appendChild(selBox);
        selBox.focus();
        selBox.select();
        document.execCommand('copy', false, null);
        document.body.removeChild(selBox);
    }

    public static filter(value: string, search: string) {
        return value.toString().toLowerCase().includes(search.toString().toLowerCase());
    };

    public static compare(direction: number, a: any, b: any) {
        if (a < b) {
            return -1 * direction;
        }
        if (a > b) {
            return direction;
        }
        return 0;
    };

    public static getDirection(direction: string): number {
        return direction === 'asc' ? 1 : -1
    }

    public static getSortType(direction: string): SortTypeEnum {
        return direction === 'asc' ? SortTypeEnum.ASC : SortTypeEnum.DESC
    }

    public static keyFromValue(stringEnum: any, value: string): string | undefined {
        for (const k of Object.keys(stringEnum)) {
            if (stringEnum[k] === value) {
                return k;
            }
        }
        return undefined;
    }

    public static getDateFormatFromString(date: string): string {
        return Utils.getDateFormat(new Date(date));
    }

    public static getDateFormat(date: Date): string {
        return Utils.datePipe.transform(date, 'dd.MM.yyyy');
    }

    public static getDateTimeFormatFromString(date: string): string {
        return Utils.getDateTimeFormat(new Date(date));
    }

    public static getDateTimeFormat(date: Date): string {
        return Utils.datePipe.transform(date, 'dd.MM.yyyy HH:mm:ss');
    }

    static getDateTimeToSend(date: Date): any {
        return Utils.datePipe.transform(date, 'yyyy-MM-dd\'T\'HH:mm:ss')
    }
}

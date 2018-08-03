import {NgbDateParserFormatter, NgbDateStruct} from '@ng-bootstrap/ng-bootstrap';
import {toInteger} from '@ng-bootstrap/ng-bootstrap/util/util';
import {Utils} from '../../../@core/utils/utils';

export class NgbDateRusParserFormatter extends NgbDateParserFormatter {
    parse(value: string): NgbDateStruct {
        if (value) {
            const dateParts = value.trim().split('.');
            return {year: toInteger(dateParts[2]), month: toInteger(dateParts[1]), day: toInteger(dateParts[0])};
        } else {
            return null;
        }
    }

    format(date: NgbDateStruct): string {
        if (date) {
            return Utils.getDateFormat(new Date(date.year, date.month - 1, date.day))
        } else {
            return null;
        }
    }

}

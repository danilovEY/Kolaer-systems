import {BaseModel} from './base.model';
import {TypeDay} from './typeday.enum';

export class HolidayModel extends BaseModel {
	name: string;
    holidayDate: Date;
    holidayType: TypeDay;
}

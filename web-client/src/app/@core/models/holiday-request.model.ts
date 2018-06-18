import {TypeDay} from './typeday.enum';
import {HolidayModel} from './holiday.model';
import {Utils} from '../utils/utils';

export class HolidayRequestModel {
	name: string;
    holidayDate: string;
    holidayType: TypeDay;

    public static createRequestModel(model: HolidayModel): HolidayRequestModel {
        const holidayRequestModel: HolidayRequestModel = new HolidayRequestModel();

        holidayRequestModel.name = model.name;
        holidayRequestModel.holidayDate = Utils.getDateTimeToSend(model.holidayDate);
        holidayRequestModel.holidayType = model.holidayType;

        return holidayRequestModel;
    }
}

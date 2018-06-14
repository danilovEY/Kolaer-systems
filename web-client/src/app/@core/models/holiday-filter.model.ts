import {FilterTypeEnum} from './filter-type.enum';

export class HolidayFilterModel {
    constructor(public filterName?: string,
                public filterHolidayDate?: string,
                public filterHolidayType?: string,
                public typeFilterHolidayType?: FilterTypeEnum) {
    }
}

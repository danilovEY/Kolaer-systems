import {SortTypeEnum} from "./sort-type.enum";

export class HolidaySortModel {
    constructor(public filterName?: SortTypeEnum,
                public filterHolidayDate?: SortTypeEnum,
                public filterHolidayType?: SortTypeEnum) {
    }
}

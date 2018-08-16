import {SortTypeEnum} from './sort-type.enum';

export class DepartmentSortModel {
    constructor(public sortId?: SortTypeEnum,
                public sortAbbreviatedName?: SortTypeEnum) {
    }
}

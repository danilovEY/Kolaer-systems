import {SortTypeEnum} from './sort-type.enum';

export class AccountSortModel {
    constructor(public sortId?: SortTypeEnum,
                public sortInitials?: SortTypeEnum,
                public sortPostName?: SortTypeEnum,
                public sortDepartmentName?: SortTypeEnum) {
    }
}

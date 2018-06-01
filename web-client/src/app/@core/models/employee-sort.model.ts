import {SortTypeEnum} from './sort-type.enum';

export class EmployeeSortModel {
    constructor(public sortId?: SortTypeEnum,
                public sortInitials?: SortTypeEnum,
                public sortPostName?: SortTypeEnum,
                public sortDepartmentName?: SortTypeEnum) {
    }
}

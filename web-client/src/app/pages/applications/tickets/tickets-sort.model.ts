import {SortTypeEnum} from '../../../@core/models/sort-type.enum';

export class TicketsSortModel {
    constructor(public sortId?: SortTypeEnum,
                public sortCount?: SortTypeEnum,
                public sortTypeOperation?: SortTypeEnum,
                public sortEmployeeInitials?: SortTypeEnum,
                public sortEmployeePost?: SortTypeEnum,
                public sortEmployeeDepartment?: SortTypeEnum) {
    }

}

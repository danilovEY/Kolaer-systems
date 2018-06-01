import {SortTypeEnum} from '../../../../@core/models/sort-type.enum';

export class BankAccountSortModel {
    constructor(public sortId?: SortTypeEnum,
                public sortCheck?: SortTypeEnum,
                public sortEmployeeInitials?: SortTypeEnum,
                public sortEmployeePost?: SortTypeEnum,
                public sortEmployeeDepartment?: SortTypeEnum) {
    }
}

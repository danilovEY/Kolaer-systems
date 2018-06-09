import {SortTypeEnum} from './sort-type.enum';

export class EmployeeSortModel {
    constructor(public sortId?: SortTypeEnum,
                public sortInitials?: SortTypeEnum,
                public sortPostName?: SortTypeEnum,
                public sortDepartmentName?: SortTypeEnum,
                public sortDismissalDate?: SortTypeEnum,
                public sortPersonnelNumber?: SortTypeEnum,
                public sortFirstName?: SortTypeEnum,
                public sortSecondName?: SortTypeEnum,
                public sortThirdName?: SortTypeEnum,
                public sortGender?: SortTypeEnum,
                public sortCategory?: SortTypeEnum,
                public sortBirthday?: SortTypeEnum,
                public sortWorkPhoneNumber?: SortTypeEnum) {
    }
}

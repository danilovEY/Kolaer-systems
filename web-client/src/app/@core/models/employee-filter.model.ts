import {FilterTypeEnum} from './filter-type.enum';
import {Gender} from "./gender.enum";
import {Category} from "./category.enum";

export class EmployeeFilterModel {
    constructor(public filterId?: number,
                public filterInitials?: string,
                public filterPostName?: string,
                public filterDepartmentName?: string,
                public filterPersonnelNumber?: number,
                public filterFirstName?: string,
                public filterSecondName?: string,
                public filterThirdName?: string,
                public filterGender?: Gender,
                public filterCategory?: Category,
                public filterWorkPhoneNumber?: string,
                public filterBirthday?: string,
                public typeFilterDismissalDate: FilterTypeEnum = FilterTypeEnum.IS_NULL) {
    }
}

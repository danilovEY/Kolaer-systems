export class EmployeeFilterModel {
    constructor(public filterId?: number,
                public filterInitials?: string,
                public filterPostName?: string,
                public filterDepartmentName?: string) {
    }
}

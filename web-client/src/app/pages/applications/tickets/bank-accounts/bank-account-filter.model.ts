export class BankAccountFilterModel {
    constructor(public filterId?: number,
                public filterCheck?: string,
                public filterDeleted: boolean = false,
                public filterEmployeeInitials?: string,
                public filterEmployeePost?: string,
                public filterEmployeeDepartment?: string) {
    }
}

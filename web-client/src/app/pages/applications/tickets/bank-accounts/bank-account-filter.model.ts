export class BankAccountFilterModel {
    constructor(public filterId?: number,
                private filterCheck?: string,
                private filterEmployeeInitials?: string,
                private filterEmployeePost?: string,
                private filterEmployeeDepartment?: string) {
    }
}

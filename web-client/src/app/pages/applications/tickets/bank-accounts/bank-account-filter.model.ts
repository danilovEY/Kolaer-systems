export class BankAccountFilterModel {
    constructor(public filterId?: number,
                private _filterCheck?: string,
                private _filterEmployeeInitials?: string,
                private _filterEmployeePost?: string,
                private _filterEmployeeDepartment?: string) {
    }


    public get filterCheck(): string {
        return this._filterCheck;
    }

    public set filterCheck(value: string) {
        this._filterCheck = `%${value}%`;
    }

    public get filterEmployeeInitials(): string {
        return this._filterEmployeeInitials;
    }

    public set filterEmployeeInitials(value: string) {
        this._filterEmployeeInitials = `%${value}%`;
    }

    public get filterEmployeePost(): string {
        return this._filterEmployeePost;
    }

    public set filterEmployeePost(value: string) {
        this._filterEmployeePost = `%${value}%`;
    }

    public get filterEmployeeDepartment(): string {
        return this._filterEmployeeDepartment;
    }

    public set filterEmployeeDepartment(value: string) {
        this._filterEmployeeDepartment = `%${value}%`;
    }
}

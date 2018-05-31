export class BankAccountFilterModel {
    constructor(public id?: number,
                public _check?: string,
                public _employeeInitials?: string,
                public _employeePost?: string,
                public _employeeDepartment?: string) {
    }


    public get check(): string {
        return this._check;
    }

    public get employeeInitials(): string {
        return this._employeeInitials;
    }

    public get employeePost(): string {
        return this._employeePost;
    }

    public get employeeDepartment(): string {
        return this._employeeDepartment;
    }

    public set check(value: string) {
        this._check = `%${value}%`;
    }

    public set employeeInitials(value: string) {
        this._employeeInitials = `%${value}%`;
    }

    public set employeePost(value: string) {
        this._employeePost = `%${value}%`;
    }

    public set employeeDepartment(value: string) {
        this._employeeDepartment = `%${value}%`;
    }
}

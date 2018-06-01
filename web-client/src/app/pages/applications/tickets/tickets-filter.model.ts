import {TypeOperationEnum} from './main/type-operation.enum';

export class TicketsFilterModel {
    constructor(public filterId?: number,
                public filterCount?: number,
                public filterTypeOperation?: TypeOperationEnum,
                private _filterEmployeeInitials?: string,
                private _filterEmployeePost?: string,
                private _filterEmployeeDepartment?: string) {
    }


    get filterEmployeeInitials(): string {
        return this._filterEmployeeInitials;
    }

    set filterEmployeeInitials(value: string) {
        this._filterEmployeeInitials = `%${value}%`;
    }

    get filterEmployeePost(): string {
        return this._filterEmployeePost;
    }

    set filterEmployeePost(value: string) {
        this._filterEmployeePost = `%${value}%`;
    }

    get filterEmployeeDepartment(): string {
        return this._filterEmployeeDepartment;
    }

    set filterEmployeeDepartment(value: string) {
        this._filterEmployeeDepartment = `%${value}%`;
    }
}

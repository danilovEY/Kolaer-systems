import {TypeOperationEnum} from './main/type-operation.enum';

export class TicketsFilterModel {
    constructor(public filterId?: number,
                public filterCount?: number,
                public filterTypeOperation?: TypeOperationEnum,
                public filterEmployeeInitials?: string,
                public filterEmployeePost?: string,
                public filterEmployeeDepartment?: string) {
    }
}

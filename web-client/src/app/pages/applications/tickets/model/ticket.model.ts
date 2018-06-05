import {BaseModel} from '../../../../@core/models/base.model';
import {EmployeeModel} from '../../../../@core/models/employee.model';
import {TypeOperationEnum} from '../main/type-operation.enum';

export class TicketModel extends BaseModel {
    public count: number;
    public type: TypeOperationEnum;
    public employee: EmployeeModel;
}

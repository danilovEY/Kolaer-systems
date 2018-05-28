import {BaseModel} from '../../../@core/models/base.model';
import {EmployeeModel} from '../../../@core/models/employee.model';
import {TypeOperationEnum} from './type-operation.enum';

export class TicketModel extends BaseModel {
    public count: number;
    public typeOperation: TypeOperationEnum;
    public employee: EmployeeModel;
}

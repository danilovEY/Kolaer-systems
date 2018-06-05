import {BaseModel} from '../../../../@core/models/base.model';
import {TypeOperationEnum} from '../main/type-operation.enum';

export class TicketRequestModel extends BaseModel {
    public count: number;
    public type: TypeOperationEnum;
    public employeeId: number;
}

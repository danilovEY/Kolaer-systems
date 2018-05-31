import {BaseModel} from '../../../../@core/models/base.model';

export class BankAccountRequestModel extends BaseModel {
    public check: string;
    public employeeId: number;
}

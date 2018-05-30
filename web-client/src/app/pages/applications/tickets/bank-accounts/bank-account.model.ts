import {BaseModel} from '../../../../@core/models/base.model';
import {EmployeeModel} from '../../../../@core/models/employee.model';

export class BankAccountModel extends BaseModel {
    public check: string;
    public employee: EmployeeModel;
}

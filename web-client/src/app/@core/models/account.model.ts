import {BaseModel} from './base.model';
import {EmployeeModel} from './employee.model';

export class AccountModel extends BaseModel {
	username: string;
	chatName: string;
	email: string;
    avatarUrl: string;
	employee: EmployeeModel;
	accessOit: boolean;
	accessUser: boolean;
}

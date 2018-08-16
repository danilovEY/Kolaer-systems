import {BaseModel} from './base.model';

export class SimpleAccountModel extends BaseModel {
	username: string;
	chatName: string;
	email: string;
    avatarUrl: string;
	employeeId: number;
	accessOit: boolean;
	accessUser: boolean;
	accessOk: boolean;
    accessVacationAdmin: boolean;
    accessVacationDepEdit: boolean;
}

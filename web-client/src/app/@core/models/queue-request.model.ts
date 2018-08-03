import {BaseModel} from './base.model';
import {AccountModel} from './account.model';

export class QueueRequestModel extends BaseModel {
    public account: AccountModel;
    public comment: string;
    public queueFrom: Date;
    public queueTo: Date;
}

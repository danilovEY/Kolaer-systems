import {BaseModel} from './base.model';
import {SimpleAccountModel} from './simple-account.model';

export class QueueRequestModel extends BaseModel {
    public account: SimpleAccountModel;
    public comment: string;
    public queueFrom: Date;
    public queueTo: Date;
}

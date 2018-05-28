import {BaseModel} from '../../../@core/models/base.model';

export class TicketRegisterModel extends BaseModel {
    public close: boolean = false;
    public createRegister: Date;
    public sendRegisterTime: Date;
    public attachmentId: number;

}

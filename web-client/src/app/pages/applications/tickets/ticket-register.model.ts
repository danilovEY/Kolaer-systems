import {BaseModel} from '../../../@core/models/base.model';
import {UploadFileModel} from '../../../@core/models/upload-file.model';

export class TicketRegisterModel extends BaseModel {
    public close: boolean = false;
    public createRegister: Date;
    public sendRegisterTime: Date;
    public attachment: UploadFileModel;

}

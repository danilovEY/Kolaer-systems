import {BaseModel} from './base.model';

export class UploadFileModel extends BaseModel {
    public fileName: string;
    public fileCreate: string;
    public accountId: number;
}

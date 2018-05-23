import {BaseModel} from '../../../@core/models/base.model';

export class PasswordHistoryModel extends BaseModel {
    constructor(public login?: string,
                public password?: string,
                public passwordWriteDate?: Date) {
        super();
    }
}

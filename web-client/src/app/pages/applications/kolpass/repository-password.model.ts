import {BaseModel} from '../../../@core/models/base.model';
import {AccountModel} from "../../../@core/models/account.model";

export class RepositoryPasswordModel extends BaseModel {
    constructor(public account: AccountModel,
                public name: string,
                public urlImage: string) {
        super();
    }    
}

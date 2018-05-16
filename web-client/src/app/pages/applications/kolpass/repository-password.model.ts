import {BaseModel} from '../../../@core/models/base.model';

export class RepositoryPasswordModel extends BaseModel {
    constructor(public accountId: number,
                public name: string,
                public urlImage: string) {
        super();
    }    
}

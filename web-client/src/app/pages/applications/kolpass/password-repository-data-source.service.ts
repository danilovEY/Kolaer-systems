import {DataSource} from 'ng2-smart-table/lib/data-source/data-source';

export class PasswordRepositoryDataSourceService extends DataSource {

    constructor() {
        super();
    }

    getAll(): Promise<any> {
        return undefined;
    }

    getElements(): Promise<any> {
        return undefined;
    }

    getSort(): any {
        return undefined;
    }

    getFilter(): any {
        return undefined;
    }

    getPaging(): any {
        return undefined;
    }

    count(): number {
        return undefined;
    }

}

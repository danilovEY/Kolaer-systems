import {DataSource} from 'ng2-smart-table/lib/data-source/data-source';
import {Deferred} from 'ng2-smart-table/lib/helpers';

export class TableEventEditModel<T> {
    data: T;
    newData: T;
    source: DataSource;
    confirm: Deferred;
}

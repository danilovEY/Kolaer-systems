import {DataSource} from 'ng2-smart-table/lib/data-source/data-source';
import {Deferred} from 'ng2-smart-table/lib/helpers';

export class TableEventDeleteModel<T> {
    data: T;
    source: DataSource;
    confirm: Deferred;
}

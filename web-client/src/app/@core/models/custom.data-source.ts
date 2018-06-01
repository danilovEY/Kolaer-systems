import {LocalDataSource} from 'ng2-smart-table';
import {Subject} from 'rxjs/Subject';
import {Observable} from 'rxjs/Observable';
import {Page} from './page.model';
import {BaseModel} from './base.model';
import {ColumnSort} from '../../@theme/components/table/column-sort';

export abstract class CustomDataSource<T extends BaseModel> extends LocalDataSource {
    private initDataSource: boolean = false;

    protected readonly onChangedLoading = new Subject<boolean>();
    protected dataPage: Page<T> = {
        data: [],
        number: 0,
        pageSize: 1,
        total: 0
    };

    constructor(protected defaultSortConfig?: ColumnSort) {
        super();
    }

    protected paginate(data: Array<any>): Array<any> {
        return data;
    }

    onLoading(): Observable<boolean> {
        return this.onChangedLoading.asObservable();
    }


    setSort(conf: Array<any>, doEmit?: boolean): LocalDataSource {
        if (!this.initDataSource) {
            this.initDataSource = true;
            return this.defaultSortConfig ? super.setSort([this.defaultSortConfig], doEmit) : this;
        } else {
            return super.setSort(conf, doEmit);
        }
    }

    getElements(): Promise<any> {
        this.onChangedLoading.next(true);
        return this.loadElements(this.getPage(), this.getPageSize());
    }

    getPage(): number {
        return this.pagingConf['page'];
    }

    getPageSize() {
        return this.pagingConf['perPage'];
    }

    abstract loadElements(page: number, pageSize: number): Promise<T[]>;

    setData(response: Page<T>): T[] {
        this.dataPage = response;
        this.data = response.data;

        this.onChangedLoading.next(false);

        return this.data
    }

    getAll(): Promise<any> {
        return super.getAll();
    }

    count(): number {
        return this.dataPage.total;
    }
}

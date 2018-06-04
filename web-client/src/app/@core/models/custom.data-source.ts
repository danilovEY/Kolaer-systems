import {LocalDataSource} from 'ng2-smart-table';
import {Subject} from 'rxjs/Subject';
import {Observable} from 'rxjs/Observable';
import {Page} from './page.model';
import {BaseModel} from './base.model';
import {ColumnSort} from '../../@theme/components/table/column-sort';
import {TableFilters} from "../../@theme/components/table/table-filter";
import {Utils} from "../utils/utils";

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

    getFilterModel<C>(filterModel: C): C {
        const tableFilters: TableFilters = this.getFilter();

        if (tableFilters.filters.length > 0) {
            for (const filer of tableFilters.filters) {
                const fieldName: string = 'filter' +
                    filer.field.charAt(0).toUpperCase() +
                    filer.field.slice(1);

                filterModel[fieldName] = filer.search;
            }
        }

        return filterModel;
    }

    getSortModel<C>(sortModel: C): C {
        const columnSorts: ColumnSort[] = this.getSort();

        if (columnSorts.length > 0) {
            const columnSort: ColumnSort = columnSorts[0];

            const fieldName: string = 'sort' +
                columnSort.field.charAt(0).toUpperCase() +
                columnSort.field.slice(1);

            sortModel[fieldName] = Utils.getSortType(columnSort.direction);
        }

        return sortModel;
    }
}

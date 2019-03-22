import {LocalDataSource} from 'ng2-smart-table';
import {Page} from './page.model';
import {ColumnSort} from '../../@theme/components/table/column-sort';
import {TableFilters} from '../../@theme/components/table/table-filter';
import {Utils} from '../utils/utils';
import {Observable, Subject} from 'rxjs/index';

export abstract class CustomDataSource<T> extends LocalDataSource {
    private initDataSource: boolean = false;

    protected readonly onChangedLoading = new Subject<boolean>();
    protected dataPage: Page<T> = new Page<T>();
    private currentSort: any;
    private currentFilter: any;
    private isRefresh: boolean = false;

    constructor(protected defaultSortConfig?: ColumnSort) {
        super();

        this.currentSort = this.getSort();
        this.currentFilter = this.getFilter();
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
            return this.defaultSortConfig ? super.setSort([this.defaultSortConfig], doEmit) : null as LocalDataSource;
        } else {
            return super.setSort(conf, doEmit);
        }
    }

    addFilter(fieldConf: any, andOperator?: boolean, doEmit?: boolean): LocalDataSource {
        this.currentFilter = null;
        return super.addFilter(fieldConf, andOperator, doEmit);
    }

    getElements(): Promise<any> {
        if (this.isRefresh) {
            this.isRefresh = false;
            this.onChangedLoading.next(true);
            return this.loadElements(this.getPage(), this.getPageSize());
        }

        const sort = this.getSort();
        const filter = this.getFilter();

        if (this.getPage() === this.dataPage.pageNum &&
            this.getPageSize() === this.dataPage.pageSize &&
            sort === this.currentSort &&
            filter === this.currentFilter) {
            return super.getElements();
        }

        this.currentSort = sort;
        this.currentFilter = filter;

        this.onChangedLoading.next(true);
        return this.loadElements(this.getPage(), this.getPageSize());
    }


    refreshFromServer(): void {
        this.isRefresh = true;
        super.refresh();
    }

    getPage(): number {
        return this.pagingConf['page'];
    }

    getPageSize(): number {
        return this.pagingConf['perPage'];
    }

    abstract loadElements(page: number, pageSize: number): Promise<T[]>;

    setData(response: T[]): T[] {
        this.data = response;
        this.dataPage.data = response;
        this.dataPage.total = response.length;
        this.dataPage.pageNum = 1;

        this.onChangedLoading.next(false);

        return this.data
    }

    setDataPage(response: Page<T>): T[] {
        this.data = response.data;
        this.dataPage = response;

        this.onChangedLoading.next(false);

        return this.data
    }

    getAll(): Promise<any> {
        return super.getAll();
    }

    count(): number {
        return this.dataPage.total;
    }

    getSort(): ColumnSort[] {
        return super.getSort();
    }

    getFilter(): TableFilters {
        return super.getFilter();
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

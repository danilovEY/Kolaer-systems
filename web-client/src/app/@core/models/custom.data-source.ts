import {LocalDataSource} from 'ng2-smart-table';
import {Subject} from 'rxjs/Subject';
import {Observable} from 'rxjs/Observable';
import {Page} from './page.model';
import {BaseModel} from './base.model';
import {SortTypeEnum} from "./sort-type.enum";

export abstract class CustomDataSource<T extends BaseModel> extends LocalDataSource {
    protected readonly onChangedLoading = new Subject<boolean>();
    protected dataPage: Page<T> = {
        data: [],
        number: 0,
        pageSize: 1,
        total: 0
    };

    public static FILTER = (value: string, search: string) => {
        return value.toString().toLowerCase().includes(search.toString().toLowerCase());
    };

    public static COMPARE = (direction: number, a: any, b: any) => {
        if (a < b) {
            return -1 * direction;
        }
        if (a > b) {
            return direction;
        }
        return 0;
    };

    public static getDirection(direction: string): number {
        return direction === 'asc' ? 1 : -1
    }

    public static getSortType(direction: string): SortTypeEnum {
        return direction === 'asc' ? SortTypeEnum.ASC : SortTypeEnum.DESC
    }

    protected paginate(data: Array<any>): Array<any> {
        return data;
    }

    onLoading(): Observable<boolean> {
        return this.onChangedLoading.asObservable();
    }

    getElements(): Promise<any> {
        // if (this.dataPage.number !== this.getPage() || this.dataPage.pageSize !== this.getPageSize()) {
            this.onChangedLoading.next(true);
            return this.loadElements(this.getPage(), this.getPageSize());
        // } else {
        //     return super.getElements();
        // }
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

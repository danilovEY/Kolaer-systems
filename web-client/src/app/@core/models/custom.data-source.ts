import {LocalDataSource} from 'ng2-smart-table';
import {Subject} from 'rxjs/Subject';
import {Observable} from 'rxjs/Observable';
import {Page} from './page.model';
import {BaseModel} from './base.model';

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

    public static getDirection(direction: string) {
        return direction === 'asc' ? 1 : -1
    }



    protected paginate(data: Array<any>): Array<any> {
        return data;
    }

    onLoading(): Observable<boolean> {
        return this.onChangedLoading.asObservable();
    }

    getElements(): Promise<any> {
        if (this.dataPage.number !== this.pagingConf['page'] || this.dataPage.pageSize !== this.pagingConf['perPage']) {
            this.onChangedLoading.next(true);
            return this.loadElements(this.pagingConf['page'], this.pagingConf['perPage']);
        } else {
            return super.getElements();
        }
    }

    abstract loadElements(page: number, pageSize: number): Promise<T[]>;

    setData(response: Page<T>): T[] {
        this.dataPage = response;
        this.data = response.data;

        this.onChangedLoading.next(false);

        return this.data
    }

    getFilteredAndSorted(): Promise<any> {
        return super.getFilteredAndSorted();
    }

    getAll(): Promise<any> {
        return super.getAll();
    }

    count(): number {
        return this.dataPage.total;
    }
}

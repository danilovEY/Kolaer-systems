import {LocalDataSource} from 'ng2-smart-table';
import {OnInit} from '@angular/core';
import {KolpassService} from './kolpass.service';
import {Page} from '../../../@core/models/page.model';
import {Subject} from 'rxjs/Subject';
import {Observable} from 'rxjs/Observable';
import {PasswordHistoryModel} from './password-history.model';

export class PasswordHistoryDataSource extends LocalDataSource implements OnInit {
    private readonly onChangedLoading = new Subject<boolean>();
    private dataPage: Page<PasswordHistoryModel> = {
        data: [],
        number: 0,
        pageSize: 1,
        total: 0
    };
    
    constructor(private repId: number, private kolpassService: KolpassService) {
        super();
        this.ngOnInit();
    }

    ngOnInit(): void {

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
            return this.kolpassService.getHistoryInRepository(this.repId, this.pagingConf['page'], this.pagingConf['perPage'])
                .toPromise()
                .then((response: Page<PasswordHistoryModel>) => {
                    this.dataPage = response;
                    this.data = response.data;

                    this.onChangedLoading.next(false);

                    return this.data;
                });
        } else {
            return super.getElements();
        }
    }


    getFilteredAndSorted(): Promise<any> {
        console.log('getFilteredAndSorted');
        return super.getFilteredAndSorted();
    }

    getAll(): Promise<any> {
        console.log('getAll');
        return super.getAll();
    }

    count(): number {
        return this.dataPage.total;
    }
}

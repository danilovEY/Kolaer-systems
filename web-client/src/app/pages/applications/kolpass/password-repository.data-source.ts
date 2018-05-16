import {RepositoryPasswordModel} from './repository-password.model';
import {LocalDataSource} from 'ng2-smart-table';
import {OnInit} from '@angular/core';
import {KolpassService} from './kolpass.service';
import {Page} from '../../../@core/models/page.model';

export class PasswordRepositoryDataSource extends LocalDataSource implements OnInit {
    private dataPage: Page<RepositoryPasswordModel> = {
        data: [],
        page: 0,
        pageSize: 1,
        total: 0
    };
    
    constructor(private kolpassService: KolpassService) {
        super();
        this.ngOnInit();
    }

    ngOnInit(): void {

    }

    protected paginate(data: Array<any>): Array<any> {
        return data;
    }


    getElements(): Promise<any> {
        console.log('getElements');
        console.log(this.dataPage);

        if (this.dataPage.page !== Number(this.pagingConf['page'])) {
            return this.kolpassService.getAllMyRepositories(this.pagingConf['page'], this.pagingConf['perPage'])
                .toPromise()
                .then((response: Page<RepositoryPasswordModel>) => {
                    this.dataPage = response;

                    this.data = this.dataPage.data;
                    console.log(this.dataPage);

                    return this.dataPage.data;
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

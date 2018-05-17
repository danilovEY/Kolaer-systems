import {Component, OnInit, ViewChild} from '@angular/core';
import {PasswordRepositoryDataSource} from './password-repository.data-source';
import {DataSource} from 'ng2-smart-table/lib/data-source/data-source';
import {KolpassService} from './kolpass.service';
import {CustomTableComponent} from '../../../@theme/components';

@Component({
    selector: 'app-kolpass',
    styleUrls: ['./kolpass.component.scss'],
    templateUrl: './kolpass.component.html'
})
export class KolpassComponent implements OnInit {
    
    @ViewChild('customTable')
    customTable: CustomTableComponent;
    
    loading: boolean = true;

    source: DataSource;


    constructor(private kolpassService: KolpassService) {
        const source: PasswordRepositoryDataSource = new PasswordRepositoryDataSource(kolpassService);
        source.onLoading().subscribe(load => this.loading = load);

        this.source = source;
    }

    ngOnInit(): void {

    }

}

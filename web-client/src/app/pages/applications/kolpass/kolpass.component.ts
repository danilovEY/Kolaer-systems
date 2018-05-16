import {Component, OnInit, ViewChild} from '@angular/core';
import {Ng2SmartTableComponent} from 'ng2-smart-table/ng2-smart-table.component';
import {PasswordRepositoryDataSource} from './password-repository.data-source';
import {DataSource} from 'ng2-smart-table/lib/data-source/data-source';
import {KolpassService} from "./kolpass.service";

@Component({
    selector: 'app-kolpass',
    styleUrls: ['./kolpass.component.scss'],
    templateUrl: './kolpass.component.html'
})
export class KolpassComponent implements OnInit {
    
    @ViewChild('table')
    table: Ng2SmartTableComponent;

    loading: boolean = false;

    settings = {
        actions: {
            columnTitle: 'Действия',
            add: true,
            edit: true,
            delete: true,
            custom: [],
            position: 'right',
        },
        pager: {
            display: true,
            perPage: 1,
        },
        noDataMessage: 'Парольница пустая',
        selectMode: 'single',
        add: {
            addButtonContent: '<i class="nb-plus"></i>',
            createButtonContent: '<i class="nb-checkmark"></i>',
            cancelButtonContent: '<i class="nb-close"></i>',
        },
        edit: {
            editButtonContent: '<i class="nb-edit"></i>',
            saveButtonContent: '<i class="nb-checkmark"></i>',
            cancelButtonContent: '<i class="nb-close"></i>',
        },
        delete: {
            deleteButtonContent: '<i class="nb-trash"></i>',
            confirmDelete: true,
        },
        columns:  {
            id: {
                title: 'ID',
                type: 'number',
            },
            name: {
                title: 'Name',
                type: 'string',
            }
        },
    };

    source: DataSource;


    constructor(private kolpassService: KolpassService) {
        this.source = new PasswordRepositoryDataSource(kolpassService);
        this.source.onChanged().subscribe(value => console.log(value.action));
    }

    onDeleteConfirm(event): void {
        if (window.confirm('Are you sure you want to delete?')) {
            event.confirm.resolve();
        } else {
            event.confirm.reject();
        }
    }

    ngOnInit(): void {

    }

}

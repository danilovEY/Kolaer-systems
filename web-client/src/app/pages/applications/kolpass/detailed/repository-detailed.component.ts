import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {KolpassService} from '../kolpass.service';
import {ToasterConfig} from "angular2-toaster";
import {Column} from "ng2-smart-table/lib/data-set/column";
import {DataSource} from "ng2-smart-table/lib/data-source/data-source";
import {CustomTableComponent} from "../../../../@theme/components";
import {PasswordHistoryDataSource} from "../password-history.data-source";
import {DatePipe} from "@angular/common";

@Component({
    selector: 'repository-detailed',
    styleUrls: ['./repository-detailed.component.scss'],
    templateUrl: './repository-detailed.component.html'
})
export class RepositoryDetailedComponent implements OnInit, OnDestroy {
    private sub: any;

    @ViewChild('customTable')
    customTable: CustomTableComponent;


    columns: Column[] = [];
    source: DataSource;
    loadingLastPass: boolean = false;
    loadingHistory: boolean = false;
    config: ToasterConfig = new ToasterConfig({
        positionClass: 'toast-top-right',
        timeout: 5000,
        newestOnTop: true,
        tapToDismiss: true,
        preventDuplicates: false,
        animation: 'fade',
        limit: 5,
    });

    constructor(private activatedRoute: ActivatedRoute,
                private kolpassService: KolpassService) {
        this.sub = this.activatedRoute.params.subscribe(params => {
            this.source = new PasswordHistoryDataSource(params['id'], this.kolpassService);
        });
    }

    ngOnInit() {
        const idColumn: Column = new Column('id', {
            title: 'ID',
            type: 'number',
            editable: false,
            addable: false,
            width: '15px',
        }, undefined);

        const dateColumn: Column = new Column('passwordWriteDate', {
            title: 'Время создания',
            type: 'date',
            valuePrepareFunction(value: number) {
                const datePipe = new DatePipe('en-US');
                return datePipe.transform(new Date(value), 'dd.MM.yyyy hh:mm:ss');
            }
        }, undefined);

        const loginColumn: Column = new Column('login', {
            title: 'Логин',
            type: 'string',
        }, undefined);

        const passColumn: Column = new Column('password', {
            title: 'Пароль',
            type: 'string',
        }, undefined);

        this.columns.push(idColumn, loginColumn, passColumn, dateColumn);
    }

    ngOnDestroy() {
        this.sub.unsubscribe();
    }
    
}

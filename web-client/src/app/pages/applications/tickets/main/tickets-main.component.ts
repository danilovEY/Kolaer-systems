import {Component, OnInit, ViewChild} from '@angular/core';
import {CustomTableComponent} from '../../../../@theme/components';
import {DatePipe} from '@angular/common';
import {Column} from 'ng2-smart-table/lib/data-set/column';
import {ToasterConfig} from 'angular2-toaster';
import {TicketsRegisterDataSource} from '../tickents-register.data-source';
import {TableEventDeleteModel} from '../../../../@theme/components/table/table-event-delete.model';
import {TicketRegisterModel} from '../ticket-register.model';
import {TicketsService} from '../tickets.service';

@Component({
    selector: 'main-tickets',
    styleUrls: ['./tickents-main.component.scss'],
    templateUrl: './tickents-main.component.html'
})
export class TicketsMainComponent implements OnInit {
    @ViewChild('customTable')
    customTable: CustomTableComponent;

    columns: Column[] = [];
    source: TicketsRegisterDataSource;
    loadingRegisters: boolean = true;
    config: ToasterConfig = new ToasterConfig({
        positionClass: 'toast-top-right',
        timeout: 5000,
        newestOnTop: true,
        tapToDismiss: true,
        preventDuplicates: false,
        animation: 'fade',
        limit: 5,
    });

    constructor(private ticketsService: TicketsService) {
        this.source = new TicketsRegisterDataSource(this.ticketsService);
        this.source.onLoading().subscribe(value => this.loadingRegisters = value);
    }

    ngOnInit() {
        const idColumn: Column = new Column('id', {
            title: 'ID',
            type: 'number',
            editable: false,
            addable: false,
        }, undefined);

        const dateCreateColumn: Column = new Column('createRegister', {
            title: 'Время создания',
            type: 'date',
            editable: false,
            addable: false,
            valuePrepareFunction(value: number) {
                const datePipe = new DatePipe('en-US');
                return datePipe.transform(new Date(value), 'dd.MM.yyyy hh:mm:ss');
            }
        }, undefined);

        const dateSendColumn: Column = new Column('sendRegisterTime', {
            title: 'Время отправления',
            type: 'date',
            editable: false,
            addable: false,
            valuePrepareFunction(value: number) {
                const datePipe = new DatePipe('en-US');
                return datePipe.transform(new Date(value), 'dd.MM.yyyy hh:mm:ss');
            }
        }, undefined);

        this.columns.push(idColumn, dateCreateColumn, dateSendColumn);
    }

    delete(event: TableEventDeleteModel<TicketRegisterModel>) {

    }


}

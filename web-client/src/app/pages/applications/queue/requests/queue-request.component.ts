import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {Column} from 'ng2-smart-table/lib/data-set/column';
import {CustomTableComponent} from '../../../../@theme/components';
import {ToasterConfig, ToasterService} from 'angular2-toaster';
import {CustomActionModel} from '../../../../@theme/components/table/custom-action.model';
import {ActivatedRoute, Router} from '@angular/router';
import {AccountService} from '../../../../@core/services/account.service';
import {QueueService} from '../queue.service';
import {QueueRequestDataSource} from './queue-request.data-source';
import {Subscription} from 'rxjs/index';
import {TableEventEditModel} from '../../../../@theme/components/table/table-event-edit.model';
import {QueueRequestModel} from '../../../../@core/models/queue-request.model';
import {TableEventDeleteModel} from '../../../../@theme/components/table/table-event-delete.model';
import {TableEventAddModel} from '../../../../@theme/components/table/table-event-add.model';

@Component({
    selector: 'queue-main',
    templateUrl: './queue-request.component.html',
    styleUrls: ['./queue-request.component.scss']
})
export class QueueRequestComponent implements OnInit, OnDestroy {
    private sub: Subscription;
    private targetId: number;

    @ViewChild('customTable')
    customTable: CustomTableComponent;

    columns: Column[] = [];
    actions: CustomActionModel[] = [];
    source: QueueRequestDataSource;
    loadingQueueRequest: boolean = true;
    config: ToasterConfig = new ToasterConfig({
        positionClass: 'toast-top-right',
        timeout: 5000,
        newestOnTop: true,
        tapToDismiss: true,
        preventDuplicates: false,
        animation: 'fade',
        limit: 5,
    });

    constructor(private queueService: QueueService,
                private toasterService: ToasterService,
                private router: Router,
                private accountService: AccountService,
                private activatedRoute: ActivatedRoute) {
        this.sub = this.activatedRoute.params.subscribe(params => {
            this.targetId = params['id'];

            this.source = new QueueRequestDataSource(this.targetId, this.queueService);
            this.source.onLoading().subscribe(value => this.loadingQueueRequest = value);
        });
    }

    ngOnDestroy() {
        this.sub.unsubscribe();
    }

    ngOnInit() {
        const nameColumn: Column = new Column('comment', {
            title: 'Комментарий',
            type: 'string',
            editable: true,
            addable: true,
            filter: false,
            sort: false,
            onComponentInitFunction(instance) {
                console.log('test:' + instance);
            }
        }, undefined);

        this.columns.push(nameColumn);
    }

    edit(event: TableEventEditModel<QueueRequestModel>) {
        event.confirm.resolve(event.newData);
    }

    delete(event: TableEventDeleteModel<QueueRequestModel>) {
        event.confirm.resolve();
    }

    create(event: TableEventAddModel<QueueRequestModel>) {
        event.confirm.resolve(event.newData);
    }

}

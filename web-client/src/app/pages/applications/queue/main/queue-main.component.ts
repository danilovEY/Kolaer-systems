import {Component, ViewChild} from '@angular/core';
import {Column} from 'ng2-smart-table/lib/data-set/column';
import {CustomTableComponent} from '../../../../@theme/components';
import {ToasterConfig, ToasterService} from 'angular2-toaster';
import {CustomActionModel} from '../../../../@theme/components/table/custom-action.model';
import {QueueTargetDataSource} from './queue-target.data-source';
import {Router} from '@angular/router';
import {AccountService} from '../../../../@core/services/account.service';
import {QueueService} from '../queue.service';
import {CustomActionEventModel} from '../../../../@theme/components/table/custom-action-event.model';
import {QueueTargetModel} from '../../../../@core/models/queue-target.model';

@Component({
    selector: 'queue-main',
    templateUrl: './queue-main.component.html',
    styleUrls: ['./queue-main.component.scss']
})
export class QueueMainComponent {
    private static readonly openQueueTargetActionName = 'open';

    @ViewChild('customTable')
    customTable: CustomTableComponent;

    columns: Column[] = [];
    actions: CustomActionModel[] = [];
    source: QueueTargetDataSource;
    loadingQueueTarget: boolean = true;
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
                private accountService: AccountService) {
        this.source = new QueueTargetDataSource(this.queueService);
        this.source.onLoading().subscribe(value => this.loadingQueueTarget = value);
    }

    ngOnInit() {
        const nameColumn: Column = new Column('name', {
            title: 'Наименование',
            type: 'string',
            editable: false,
            addable: false,
            filter: false,
            sort: false
        }, undefined);

        this.columns.push(nameColumn);

        const openAction: CustomActionModel = new CustomActionModel();
        openAction.name = QueueMainComponent.openQueueTargetActionName;
        openAction.content = '<i class="fa fa-eye"></i>';
        openAction.description = 'Открыть';

        this.actions.push(openAction);
    }

    action(event: CustomActionEventModel<QueueTargetModel>) {
        if (event.action.name === QueueMainComponent.openQueueTargetActionName) {
            this.router.navigate([`/pages/app/queue/${event.data.id}/request`]);
        }
    }
}

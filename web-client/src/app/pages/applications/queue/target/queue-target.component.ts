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
import {TableEventDeleteModel} from '../../../../@theme/components/table/table-event-delete.model';
import {TableEventAddModel} from '../../../../@theme/components/table/table-event-add.model';
import {TableEventEditModel} from '../../../../@theme/components/table/table-event-edit.model';
import {SimpleAccountModel} from '../../../../@core/models/simple-account.model';

@Component({
    selector: 'queue-target',
    templateUrl: './queue-target.component.html',
    styleUrls: ['./queue-target.component.scss']
})
export class QueueTargetComponent {
    private static readonly openQueueTargetActionName = 'open';


    private static currentAccount: SimpleAccountModel;

    @ViewChild('customTable')
    customTable: CustomTableComponent;

    isAdd = false;
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
        this.accountService.getCurrentAccount()
            .subscribe(account => {
                QueueTargetComponent.currentAccount = account;
                if (account.accessOit) {
                    this.isAdd = true;

                    this.customTable.table.initGrid();
                }
            });

        const nameColumn: Column = new Column('name', {
            title: 'Наименование',
            type: 'string',
            editable: true,
            addable: true,
            filter: false,
            sort: false
        }, undefined);

        this.columns.push(nameColumn);

        const openAction: CustomActionModel = new CustomActionModel();
        openAction.name = QueueTargetComponent.openQueueTargetActionName;
        openAction.content = '<i class="fa fa-eye"></i>';
        openAction.description = 'Открыть';

        this.actions.push(openAction);

        this.customTable.actionBeforeValueView = this.actionBeforeValueView;
    }

    action(event: CustomActionEventModel<QueueTargetModel>) {
        if (event.action.name === QueueTargetComponent.openQueueTargetActionName) {
            this.router.navigate([`/pages/app/queue/target/${event.data.id}/request`]);
        }
    }

    actionBeforeValueView(event: CustomActionEventModel<QueueTargetModel>) {
        if (event.action.name === CustomTableComponent.EDIT_ACTION_NAME ||
            event.action.name === CustomTableComponent.DELETE_ACTION_NAME) {

            return QueueTargetComponent.currentAccount ? QueueTargetComponent.currentAccount.accessOit : false;
        }

        return true;
    }

    edit(event: TableEventEditModel<QueueTargetModel>) {
        this.queueService.updateQueueTargets(event.data.id, event.newData)
            .subscribe(event.confirm.resolve);
    }

    delete(event: TableEventDeleteModel<QueueTargetModel>) {
        this.queueService.deleteQueueTargets(event.data.id)
            .subscribe(response => event.confirm.resolve());
    }

    create(event: TableEventAddModel<QueueTargetModel>) {
        this.queueService.addQueueTargets(event.newData)
            .subscribe(event.confirm.resolve);
    }
}

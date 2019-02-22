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
import {Utils} from '../../../../@core/utils/utils';
import {Cell} from 'ng2-smart-table';
import {DateTimeEditComponent} from '../../../../@theme/components/table/date-time-edit.component';
import {CustomActionEventModel} from '../../../../@theme/components/table/custom-action-event.model';
import {SimpleAccountModel} from '../../../../@core/models/simple-account.model';
import {SmartTableService} from '../../../../@core/services/smart-table.service';

@Component({
    selector: 'queue-main',
    templateUrl: './queue-request.component.html',
    styleUrls: ['./queue-request.component.scss']
})
export class QueueRequestComponent implements OnInit, OnDestroy {
    private static currentAccount: SimpleAccountModel;

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
        this.accountService.getCurrentAccount()
            .subscribe(account => {
                QueueRequestComponent.currentAccount = account;
                this.customTable.table.initGrid();
            });

        const queueFromColumn: Column = new Column('queueFrom', {
            title: 'Начало',
            type: 'string',
            editable: true,
            addable: true,
            filter: false,
            sort: false,
            editor: {
                type: 'custom',
                component: DateTimeEditComponent,
            },
            valuePrepareFunction(a: any, value: QueueRequestModel, cell: Cell) {
                return Utils.getDateTimeWithOutSecondFormat(value.queueFrom);
            }
        }, undefined);

        const queueToColumn: Column = new Column('queueTo', {
            title: 'Конец',
            type: 'string',
            editable: true,
            addable: true,
            filter: false,
            sort: false,
            editor: {
                type: 'custom',
                component: DateTimeEditComponent,
            },
            valuePrepareFunction(a: any, value: QueueRequestModel, cell: Cell) {
                return Utils.getDateTimeWithOutSecondFormat(value.queueTo);
            }
        }, undefined);

        const commentColumn: Column = new Column('comment', {
            title: 'Комментарий',
            type: 'string',
            editable: true,
            addable: true,
            filter: false,
            sort: false
        }, undefined);

        const employeeColumn: Column = new Column('employee', {
            title: 'Сотрудник',
            type: 'string',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
            valuePrepareFunction(a: any, value: QueueRequestModel, cell: Cell) {
                return value.account.employee
                    ? value.account.employee.initials
                    : value.account.chatName;
            }
        }, undefined);

        const postColumn: Column = new Column('employeePost', {
            title: 'Должность',
            type: 'string',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
            valuePrepareFunction(a: any, value: QueueRequestModel, cell: Cell) {
                return value.account.employee ? value.account.employee.post.abbreviatedName : '';
            }
        }, undefined);

        const departmentColumn: Column = new Column('employeeDepartment', {
            title: 'Подразделние',
            type: 'string',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
            valuePrepareFunction(a: any, value: QueueRequestModel, cell: Cell) {
                return value.account.employee ? value.account.employee.department.abbreviatedName : '';
            }
        }, undefined);

        this.columns.push(queueFromColumn,
            queueToColumn,
            commentColumn,
            employeeColumn,
            postColumn,
            departmentColumn);

        this.customTable.actionBeforeValueView = this.actionBeforeValueView;

    }

    actionBeforeValueView(event: CustomActionEventModel<QueueRequestModel>) {
        if (event.action.name === SmartTableService.EDIT_ACTION_NAME ||
            event.action.name === SmartTableService.DELETE_ACTION_NAME) {

            return QueueRequestComponent.currentAccount ?
                event.data.account.id === QueueRequestComponent.currentAccount.id
                : false;
        }

        return true;
    }

    edit(event: TableEventEditModel<QueueRequestModel>) {
        this.queueService.updateQueueRequest(this.targetId, event.data.id, event.newData)
            .subscribe(event.confirm.resolve);
    }

    delete(event: TableEventDeleteModel<QueueRequestModel>) {
        this.queueService.deleteQueueRequest(this.targetId, event.data.id)
            .subscribe(response => event.confirm.resolve());
    }

    create(event: TableEventAddModel<QueueRequestModel>) {
        this.queueService.addQueueRequest(this.targetId, event.newData)
            .subscribe(event.confirm.resolve);
    }

}

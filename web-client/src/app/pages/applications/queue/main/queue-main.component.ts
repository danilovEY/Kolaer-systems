import {Component, ViewChild} from '@angular/core';
import {Router} from '@angular/router';
import {AccountService} from '../../../../@core/services/account.service';
import {QueueService} from '../queue.service';
import {Column} from 'ng2-smart-table/lib/data-set/column';
import {CustomTableComponent} from '../../../../@theme/components';
import {SimpleAccountModel} from '../../../../@core/models/simple-account.model';
import {ToasterConfig} from 'angular2-toaster';
import {CustomActionModel} from '../../../../@theme/components/table/custom-action.model';
import {QueueMainDataSource} from './queue-main.data-source';
import {QueueRequestModel} from '../../../../@core/models/queue-request.model';
import {TableEventDeleteModel} from '../../../../@theme/components/table/table-event-delete.model';
import {TableEventEditModel} from '../../../../@theme/components/table/table-event-edit.model';
import {TableEventAddModel} from '../../../../@theme/components/table/table-event-add.model';
import {CustomActionEventModel} from '../../../../@theme/components/table/custom-action-event.model';
import {QueueScheduleModel} from '../../../../@core/models/queue-schedule.model';

@Component({
    selector: 'queue-main',
    templateUrl: './queue-main.component.html',
    styleUrls: ['./queue-main.component.scss']
})
export class QueueMainComponent {
    private static currentAccount: SimpleAccountModel;

    @ViewChild('customTable')
    customTable: CustomTableComponent;

    columns: Column[] = [];
    actions: CustomActionModel[] = [];
    source: QueueMainDataSource;
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
                private router: Router,
                private accountService: AccountService) {
        this.source = new QueueMainDataSource(this.queueService);
        this.source.onLoading().subscribe(value => this.loadingQueueRequest = value);
    }

    ngOnInit() {
        this.accountService.getCurrentAccount()
            .subscribe(account => {
                QueueMainComponent.currentAccount = account;
                this.customTable.table.initGrid();
            });

        // const queueFromColumn: Column = new Column('target', {
        //     title: 'Цель',
        //     type: 'string',
        //     editable: true,
        //     addable: true,
        //     filter: false,
        //     sort: false,
        //     editor: {
        //         type: 'custom',
        //         component: DateTimeEditComponent,
        //     },
        //     valuePrepareFunction(a: any, value: QueueScheduleModel, cell: Cell) {
        //         return Utils.getDateTimeWithOutSecondFormat(value.queueFrom);
        //     }
        // }, undefined);
        //
        // const queueFromColumn: Column = new Column('queueFrom', {
        //     title: 'Начало',
        //     type: 'string',
        //     editable: true,
        //     addable: true,
        //     filter: false,
        //     sort: false,
        //     editor: {
        //         type: 'custom',
        //         component: DateTimeEditComponent,
        //     },
        //     valuePrepareFunction(a: any, value: QueueScheduleModel, cell: Cell) {
        //         return Utils.getDateTimeWithOutSecondFormat(value.queueFrom);
        //     }
        // }, undefined);
        //
        // const queueToColumn: Column = new Column('queueTo', {
        //     title: 'Конец',
        //     type: 'string',
        //     editable: true,
        //     addable: true,
        //     filter: false,
        //     sort: false,
        //     editor: {
        //         type: 'custom',
        //         component: DateTimeEditComponent,
        //     },
        //     valuePrepareFunction(a: any, value: QueueScheduleModel, cell: Cell) {
        //         return Utils.getDateTimeWithOutSecondFormat(value.queueTo);
        //     }
        // }, undefined);
        //
        // const commentColumn: Column = new Column('comment', {
        //     title: 'Комментарий',
        //     type: 'string',
        //     editable: true,
        //     addable: true,
        //     filter: false,
        //     sort: false
        // }, undefined);
        //
        // const employeeColumn: Column = new Column('employee', {
        //     title: 'Сотрудник',
        //     type: 'string',
        //     editable: false,
        //     addable: false,
        //     filter: false,
        //     sort: false,
        //     valuePrepareFunction(a: any, value: QueueScheduleModel, cell: Cell) {
        //         return value.account.employee
        //             ? value.account.employee.initials
        //             : value.account.chatName;
        //     }
        // }, undefined);
        //
        // const postColumn: Column = new Column('employeePost', {
        //     title: 'Должность',
        //     type: 'string',
        //     editable: false,
        //     addable: false,
        //     filter: false,
        //     sort: false,
        //     valuePrepareFunction(a: any, value: QueueRequestModel, cell: Cell) {
        //         return value.account.employee ? value.account.employee.post.abbreviatedName : '';
        //     }
        // }, undefined);
        //
        // const departmentColumn: Column = new Column('employeeDepartment', {
        //     title: 'Подразделние',
        //     type: 'string',
        //     editable: false,
        //     addable: false,
        //     filter: false,
        //     sort: false,
        //     valuePrepareFunction(a: any, value: QueueRequestModel, cell: Cell) {
        //         return value.account.employee ? value.account.employee.department.abbreviatedName : '';
        //     }
        // }, undefined);
        //
        // this.columns.push(queueFromColumn,
        //     queueToColumn,
        //     commentColumn,
        //     employeeColumn,
        //     postColumn,
        //     departmentColumn);

        this.customTable.actionBeforeValueView = this.actionBeforeValueView;

    }

    actionBeforeValueView(event: CustomActionEventModel<QueueScheduleModel>) {
        if (event.action.name === CustomTableComponent.EDIT_ACTION_NAME ||
            event.action.name === CustomTableComponent.DELETE_ACTION_NAME) {

            return QueueMainComponent.currentAccount ?
                event.data.request.id === QueueMainComponent.currentAccount.id || QueueMainComponent.currentAccount.accessOit
                : false;
        }

        return true;
    }

    edit(event: TableEventEditModel<QueueRequestModel>) {
        // this.queueService.updateQueueRequest(this.targetId, event.data.id, event.newData)
        //     .subscribe(event.confirm.resolve);
    }

    delete(event: TableEventDeleteModel<QueueRequestModel>) {
        // this.queueService.deleteQueueRequest(this.targetId, event.data.id)
        //     .subscribe(response => event.confirm.resolve());
    }

    create(event: TableEventAddModel<QueueRequestModel>) {
        // this.queueService.addQueueRequest(this.targetId, event.newData)
        //     .subscribe(event.confirm.resolve);
    }

}

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
import {EmployeeEditComponent} from '../../../../@theme/components/table/employee-edit.component';
import {Cell} from 'ng2-smart-table';
import {DateTimeEditComponent} from '../../../../@theme/components/table/date-time-edit.component';

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
            editor: {
                type: 'custom',
                component: EmployeeEditComponent,
            },
            filter: false,
            sort: false,
            valuePrepareFunction(a: any, value: QueueRequestModel, cell: Cell) {
                return value.account.employee
                    ? value.account.employee.initials
                    : value.account.chatName;
            },
            // filterFunction(value: EmployeeModel, search: string) {
            //     return Utils.filter(value.initials, search);
            // },
            // compareFunction(dir: number, a: EmployeeModel, b: EmployeeModel) {
            //     return Utils.compare(dir, a.initials, b.initials);
            // }
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
    }

    edit(event: TableEventEditModel<QueueRequestModel>) {
        event.confirm.resolve(event.newData);
    }

    delete(event: TableEventDeleteModel<QueueRequestModel>) {
        event.confirm.resolve();
    }

    create(event: TableEventAddModel<QueueRequestModel>) {
        event.confirm.resolve(event.newData);
        console.log(event.newData);
    }

}

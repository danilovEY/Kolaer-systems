import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {ToasterConfig, ToasterService} from 'angular2-toaster';
import {TicketsService} from '../tickets.service';
import {TableEventAddModel} from '../../../../@theme/components/table/table-event-add.model';
import {Column} from 'ng2-smart-table/lib/data-set/column';
import {TableEventDeleteModel} from '../../../../@theme/components/table/table-event-delete.model';
import {TicketRegisterModel} from '../main/ticket-register.model';
import {CustomTableComponent} from '../../../../@theme/components';
import {ActivatedRoute} from '@angular/router';
import {TicketsDataSource} from './tickents.data-source';
import {Subscription} from 'rxjs/index';
import {Cell} from 'ng2-smart-table';
import {Utils} from '../../../../@core/utils/utils';
import {TableEventEditModel} from '../../../../@theme/components/table/table-event-edit.model';
import {TicketModel} from '../model/ticket.model';
import {TypeOperationEnum} from '../main/type-operation.enum';
import {EmployeeWithAccountEditComponent} from './employee-with-account-edit.component';
import {AccountService} from '../../../../@core/services/account.service';
import {SimpleAccountModel} from '../../../../@core/models/simple-account.model';
import {RoleConstant} from "../../../../@core/constants/role.constant";

@Component({
    selector: 'register-detailed',
    templateUrl: './register-detailed.component.html',
    styleUrls: ['./register-detailed.component.scss']
})
export class RegisterDetailedComponent implements OnInit, OnDestroy {
    private sub: Subscription;
    private registerId: number;

    @ViewChild('customTable')
    customTable: CustomTableComponent;

    columns: Column[] = [];
    selectedTicketRegister: TicketRegisterModel;
    source: TicketsDataSource;
    loadingTickets: boolean = true;
    config: ToasterConfig = new ToasterConfig({
        positionClass: 'toast-top-right',
        timeout: 5000,
        newestOnTop: true,
        tapToDismiss: true,
        preventDuplicates: false,
        animation: 'fade',
        limit: 5,
    });

    constructor(private ticketsService: TicketsService,
                private toasterService: ToasterService,
                private accountService: AccountService,
                private activatedRoute: ActivatedRoute) {
        this.sub = this.activatedRoute.params.subscribe(params => {
            this.registerId = params['id'];
        });
    }

    ngOnDestroy() {
        this.sub.unsubscribe();
    }

    ngOnInit() {
        this.ticketsService.getTicketRegister(this.registerId)
            .subscribe((response: TicketRegisterModel) => {
                this.selectedTicketRegister = response;

                this.accountService.getCurrentAccount()
                    .subscribe((account: SimpleAccountModel) => {
                        if (account.access.includes(RoleConstant.TICKET_REGISTER_WRITE) ||
                            account.id === this.selectedTicketRegister.accountId && !this.selectedTicketRegister.close) {
                            this.customTable.settings.actions.add = true;
                            this.customTable.settings.actions.edit = true;
                            this.customTable.settings.actions.delete = true;

                            this.customTable.table.initGrid();
                        }
                    });

                this.source = new TicketsDataSource(this.registerId, this.ticketsService);
                this.source.onLoading().subscribe(value => this.loadingTickets = value);
            });

        // const idColumn: Column = new Column('id', {
        //     title: 'ID',
        //     type: 'number',
        //     editable: false,
        //     addable: false,
        // }, undefined);

        const typeOperationColumn: Column = new Column('type', {
            title: 'Операция',
            type: 'string',
            filter: {
                type: 'list',
                config: {
                    selectText: 'Операция...',
                    list: [
                        { value: Utils.keyFromValue(TypeOperationEnum, TypeOperationEnum.DR), title: TypeOperationEnum.DR },
                        { value: Utils.keyFromValue(TypeOperationEnum, TypeOperationEnum.CR), title: TypeOperationEnum.CR },
                        { value: Utils.keyFromValue(TypeOperationEnum, TypeOperationEnum.ZR), title: TypeOperationEnum.ZR },
                    ],
                },
            },
            editor: {
                type: 'list',
                config: {
                    list: [
                        { value: Utils.keyFromValue(TypeOperationEnum, TypeOperationEnum.DR), title: TypeOperationEnum.DR },
                        { value: Utils.keyFromValue(TypeOperationEnum, TypeOperationEnum.CR), title: TypeOperationEnum.CR },
                        { value: Utils.keyFromValue(TypeOperationEnum, TypeOperationEnum.ZR), title: TypeOperationEnum.ZR },
                    ],
                },
            },
            valuePrepareFunction(a: any, value: TicketModel, cell: Cell) {
                return TypeOperationEnum[value.type];
            }
        }, undefined);

        const countColumn: Column = new Column('count', {
            title: 'Талоны ЛПП',
            type: 'number',
        }, undefined);

        const employeeColumn: Column = new Column('employee', {
            title: 'Сотрудник',
            type: 'string',
            editor: {
                type: 'custom',
                component: EmployeeWithAccountEditComponent,
            },
            valuePrepareFunction(a: any, value: TicketModel, cell: Cell) {
                return value.employee.initials;
            }
        }, undefined);

        const postColumn: Column = new Column('employeePost', {
            title: 'Должность',
            type: 'string',
            editable: false,
            addable: false,
            valuePrepareFunction(a: any, value: TicketModel, cell: Cell) {
                return value.employee.post.name;
            }
        }, undefined);

        const departmentColumn: Column = new Column('employeeDepartment', {
            title: 'Подразделние',
            type: 'string',
            editable: false,
            addable: false,
            valuePrepareFunction(a: any, value: TicketModel, cell: Cell) {
                return value.employee.department.name;
            }
        }, undefined);

        this.columns.push(typeOperationColumn, countColumn, employeeColumn, postColumn, departmentColumn);
    }

    delete(event: TableEventDeleteModel<TicketModel>) {
        this.ticketsService.deleteTicket(this.registerId, event.data.id)
            .subscribe(response => event.confirm.resolve());
    }

    create(event: TableEventAddModel<TicketModel>) {
        const ticketModel: TicketModel = new TicketModel();
        ticketModel.id = event.newData.id;
        ticketModel.count = event.newData.count;
        ticketModel.type = event.newData.type;
        ticketModel.employee = event.newData.employee;

        this.ticketsService.createTicket(this.registerId, ticketModel)
            .subscribe((response: TicketModel) => event.confirm.resolve(response));
    }

    edit(event: TableEventEditModel<TicketModel>) {
        this.ticketsService.updateTicket(this.registerId, event.newData)
            .subscribe((response: TicketModel) => event.confirm.resolve(response));
    }
}

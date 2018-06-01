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
import {Subscription} from 'rxjs/Subscription';
import {EmployeeEditComponent} from '../../../../@theme/components/table/employee-edit.component';
import {Cell} from 'ng2-smart-table';
import {Utils} from '../../../../@core/utils/utils';
import {EmployeeModel} from '../../../../@core/models/employee.model';
import {TableEventEditModel} from '../../../../@theme/components/table/table-event-edit.model';
import {TicketModel} from '../ticket.model';
import {TypeOperationEnum} from "../main/type-operation.enum";

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
                this.customTable.settings.actions.add = true;
                this.customTable.table.initGrid();

                this.source = new TicketsDataSource(this.registerId, this.ticketsService);
                this.source.onLoading().subscribe(value => this.loadingTickets = value);
            });

        const idColumn: Column = new Column('id', {
            title: 'ID',
            type: 'number',
            editable: false,
            addable: false,
        }, undefined);

        const typeOperationColumn: Column = new Column('type', {
            title: 'Операция',
            type: 'string',
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
            valuePrepareFunction(value: TypeOperationEnum) {
                return TypeOperationEnum[value];
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
                component: EmployeeEditComponent,
            },
            valuePrepareFunction(value: EmployeeModel) {
                return value.initials;
            },
            filterFunction(value: EmployeeModel, search: string) {
                return Utils.filter(value.initials, search);
            },
            compareFunction(dir: number, a: EmployeeModel, b: EmployeeModel) {
                return Utils.compare(dir, a.initials, b.initials);
            }
        }, undefined);

        const postColumn: Column = new Column('employee.post.name', {
            title: 'Должность',
            type: 'string',
            editable: false,
            addable: false,
            valuePrepareFunction(a: any, value: TicketModel, cell: Cell) {
                return value.employee.post.name;
            }
        }, undefined);

        const departmentColumn: Column = new Column('employee.department.name', {
            title: 'Подразделние',
            type: 'string',
            editable: false,
            addable: false,
            valuePrepareFunction(a: any, value: TicketModel, cell: Cell) {
                return value.employee.department.name;
            }
        }, undefined);

        this.columns.push(idColumn, typeOperationColumn, countColumn, employeeColumn, postColumn, departmentColumn);
    }

    delete(event: TableEventDeleteModel<TicketModel>) {
        console.log(event);
    }

    create(event: TableEventAddModel<TicketModel>) {
        console.log(event);
    }

    edit(event: TableEventEditModel<TicketModel>) {
        console.log(event);
    }
}

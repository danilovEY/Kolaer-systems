import {Component, OnInit, ViewChild} from '@angular/core';
import {CustomTableComponent} from '../../../../@theme/components';
import {Column} from 'ng2-smart-table/lib/data-set/column';
import {Toast, ToasterConfig, ToasterService} from 'angular2-toaster';
import {TableEventDeleteModel} from '../../../../@theme/components/table/table-event-delete.model';
import {TableEventAddModel} from '../../../../@theme/components/table/table-event-add.model';
import {AccountService} from '../../../../@core/services/account.service';
import {BankAccountDataSource} from './bank-account.data-source';
import {BankAccountService} from './bank-account.service';
import {BankAccountModel} from './bank-account.model';
import {EmployeeModel} from '../../../../@core/models/employee.model';
import {Cell} from 'ng2-smart-table';
import {EmployeeService} from '../../../../@core/services/employee.service';
import {EmployeeEditComponent} from '../../../../@theme/components/table/employee-edit.component';
import {TableEventEditModel} from '../../../../@theme/components/table/table-event-edit.model';
import {BankAccountRequestModel} from './bank-account-request.model';
import {Utils} from '../../../../@core/utils/utils';
import {HttpErrorResponse} from '@angular/common/http';
import {ServerExceptionModel} from '../../../../@core/models/server-exception.model';
import {Title} from "@angular/platform-browser";

@Component({
    selector: 'bank-accounts',
    styleUrls: ['./bank-accounts.component.scss'],
    templateUrl: './bank-accounts.component.html'
})
export class BankAccountsComponent implements OnInit {
    @ViewChild('customTable')
    customTable: CustomTableComponent;

    columns: Column[] = [];
    source: BankAccountDataSource;
    loadingBankAccounts: boolean = true;
    config: ToasterConfig = new ToasterConfig({
        positionClass: 'toast-top-right',
        timeout: 5000,
        newestOnTop: true,
        tapToDismiss: true,
        preventDuplicates: false,
        animation: 'fade',
        limit: 5,
    });

    constructor(private bankAccountService: BankAccountService,
                private employeeService: EmployeeService,
                private toasterService: ToasterService,
                private accountService: AccountService,
                private titleService: Title
    ) {
        this.titleService.setTitle('Банковские счета');

        this.source = new BankAccountDataSource(this.bankAccountService);
        this.source.onLoading().subscribe(value => this.loadingBankAccounts = value);
    }

    ngOnInit() {
        // const idColumn: Column = new Column('id', {
        //     title: 'ID',
        //     type: 'number',
        //     width: '10px',
        //     editable: false,
        //     addable: false,
        // }, undefined);

        const checkColumn: Column = new Column('check', {
            title: 'Номер счета',
            type: 'number',
        }, undefined);

        const employeeColumn: Column = new Column('employee', {
            title: 'Сотрудник',
            type: 'string',
            editor: {
                type: 'custom',
                component: EmployeeEditComponent,
            },
            valuePrepareFunction(a: any, value: BankAccountModel, cell: Cell) {
                return value.employee.initials;
            },
            filterFunction(value: EmployeeModel, search: string) {
                return Utils.filter(value.initials, search);
            },
            compareFunction(dir: number, a: EmployeeModel, b: EmployeeModel) {
                return Utils.compare(dir, a.initials, b.initials);
            }
        }, undefined);

        const postColumn: Column = new Column('employeePost', {
            title: 'Должность',
            type: 'string',
            editable: false,
            addable: false,
            valuePrepareFunction(a: any, value: BankAccountModel, cell: Cell) {
                return value.employee.post.name;
            }
        }, undefined);

        const departmentColumn: Column = new Column('employeeDepartment', {
            title: 'Подразделние',
            type: 'string',
            editable: false,
            addable: false,
            valuePrepareFunction(a: any, value: BankAccountModel, cell: Cell) {
                return value.employee.department.name;
            }
        }, undefined);

        this.columns.push(checkColumn, employeeColumn, postColumn, departmentColumn);
    }

    delete(event: TableEventDeleteModel<BankAccountModel>) {
        if (confirm(`Вы действительно хотите удалить счет: ${event.data.check}?`)) {
            this.bankAccountService.deleteBankAccount(event.data.id)
                .subscribe(request => event.confirm.resolve(),
                    (errorResponse: HttpErrorResponse) => {
                        event.confirm.reject();

                        const error: ServerExceptionModel = errorResponse.error;

                        const toast: Toast = {
                            type: 'error',
                            title: 'Ошибка',
                            body: error.message,
                        };

                        this.toasterService.popAsync(toast);
                    });
        } else {
            event.confirm.reject();
        }
    }

    create(event: TableEventAddModel<BankAccountModel>) {
        const bankAccount: BankAccountRequestModel = new BankAccountRequestModel();
        bankAccount.check = event.newData.check;
        bankAccount.employeeId = event.newData.employee.id;

        this.bankAccountService.addBankAccount(bankAccount)
            .subscribe((request: BankAccountModel) => event.confirm.resolve(request),
                (errorResponse: HttpErrorResponse) => {
                    event.confirm.reject();

                    const error: ServerExceptionModel = errorResponse.error;

                    const toast: Toast = {
                        type: 'error',
                        title: 'Ошибка',
                        body: error.message,
                    };

                    this.toasterService.popAsync(toast);
                });
    }

    edit(event: TableEventEditModel<BankAccountModel>) {
        const bankAccount: BankAccountRequestModel = new BankAccountRequestModel();
        bankAccount.check = event.newData.check;
        bankAccount.employeeId = event.newData.employee.id;

        this.bankAccountService.editBankAccount(event.data.id, bankAccount)
            .subscribe((request: BankAccountModel) => event.confirm.resolve(request),
                (errorResponse: HttpErrorResponse) => {
                    event.confirm.reject();

                    const error: ServerExceptionModel = errorResponse.error;

                    const toast: Toast = {
                        type: 'error',
                        title: 'Ошибка',
                        body: error.message,
                    };

                    this.toasterService.popAsync(toast);
                });
    }
}

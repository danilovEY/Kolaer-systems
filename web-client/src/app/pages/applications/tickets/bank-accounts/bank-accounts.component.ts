import {Component, OnInit, ViewChild} from '@angular/core';
import {CustomTableComponent} from '../../../../@theme/components';
import {Column} from 'ng2-smart-table/lib/data-set/column';
import {ToasterConfig} from 'angular2-toaster';
import {TableEventDeleteModel} from '../../../../@theme/components/table/table-event-delete.model';
import {CustomActionModel} from '../../../../@theme/components/table/custom-action.model';
import {TableEventAddModel} from '../../../../@theme/components/table/table-event-add.model';
import {AccountService} from '../../../../@core/services/account.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {BankAccountDataSource} from './bank-account.data-source';
import {BankAccountService} from './bank-account.service';
import {BankAccountModel} from './bank-account.model';
import {EmployeeModel} from "../../../../@core/models/employee.model";
import {Cell} from "ng2-smart-table";
import {CustomDataSource} from "../../../../@core/models/custom.data-source";
import {EmployeeService} from "../../../../@core/services/employee.service";
import {EmployeeEditComponent} from "../../../../@theme/components/table/employee-edit.component";
import {TableEventEditModel} from "../../../../@theme/components/table/table-event-edit.model";
import {BankAccountRequestModel} from "./bank-account-request.model";

@Component({
    selector: 'bank-accounts',
    styleUrls: ['./bank-accounts.component.scss'],
    templateUrl: './bank-accounts.component.html'
})
export class BankAccountsComponent implements OnInit {
    @ViewChild('customTable')
    customTable: CustomTableComponent;

    columns: Column[] = [];
    actions: CustomActionModel[] = [];
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
                private modalService: NgbModal,
                private accountService: AccountService) {
        this.source = new BankAccountDataSource(this.bankAccountService);
        this.source.onLoading().subscribe(value => this.loadingBankAccounts = value);
    }

    ngOnInit() {
        const idColumn: Column = new Column('id', {
            title: 'ID',
            type: 'number',
            width: '10px',
            editable: false,
            addable: false,
        }, undefined);

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
            valuePrepareFunction(value: EmployeeModel) {
                return value.initials;
            },
            filterFunction(value: EmployeeModel, search: string) {
                return CustomDataSource.FILTER(value.initials, search);
            },
            compareFunction(dir: number, a: EmployeeModel, b: EmployeeModel) {
                return CustomDataSource.COMPARE(dir, a.initials, b.initials);
            }
        }, undefined);

        const postColumn: Column = new Column('employee.post.name', {
            title: 'Должность',
            type: 'string',
            editable: false,
            addable: false,
            valuePrepareFunction(a: any, value: BankAccountModel, cell: Cell) {
                return value.employee.post.name;
            }
        }, undefined);

        const departmentColumn: Column = new Column('employee.department.name', {
            title: 'Подразделние',
            type: 'string',
            editable: false,
            addable: false,
            valuePrepareFunction(a: any, value: BankAccountModel, cell: Cell) {
                return value.employee.department.name;
            }
        }, undefined);

        this.columns.push(idColumn, checkColumn, employeeColumn, postColumn, departmentColumn);
    }

    delete(event: TableEventDeleteModel<BankAccountModel>) {
       this.bankAccountService.deleteBankAccount(event.data.id)
           .subscribe(request => event.confirm.resolve());
    }

    create(event: TableEventAddModel<BankAccountModel>) {
        const bankAccount: BankAccountRequestModel = new BankAccountRequestModel();
        bankAccount.check = event.newData.check;
        bankAccount.employeeId = event.newData.employee.id;

        this.bankAccountService.addBankAccount(bankAccount)
            .subscribe((request: BankAccountModel) => event.confirm.resolve(request));
    }

    edit(event: TableEventEditModel<BankAccountModel>) {
        const bankAccount: BankAccountRequestModel = new BankAccountRequestModel();
        bankAccount.check = event.newData.check;
        bankAccount.employeeId = event.newData.employee.id;

        this.bankAccountService.editBankAccount(event.data.id, bankAccount)
            .subscribe((request: BankAccountModel) => event.confirm.resolve(request));
    }
}

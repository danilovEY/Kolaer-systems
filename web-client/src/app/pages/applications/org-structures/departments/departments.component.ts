import {Component, OnInit, ViewChild} from '@angular/core';
import {Column} from 'ng2-smart-table/lib/data-set/column';
import {CustomTableComponent} from '../../../../@theme/components';
import {TableEventAddModel} from '../../../../@theme/components/table/table-event-add.model';
import {TableEventDeleteModel} from '../../../../@theme/components/table/table-event-delete.model';
import {TableEventEditModel} from '../../../../@theme/components/table/table-event-edit.model';
import {DepartmentsDataSource} from './departments.data-source';
import {DepartmentService} from '../../../../@core/services/department.service';
import {DepartmentModel} from '../../../../@core/models/department/department.model';
import {DepartmentRequestModel} from '../../../../@core/models/department-request.model';
import {AccountService} from '../../../../@core/services/account.service';
import {SimpleAccountModel} from '../../../../@core/models/simple-account.model';
import {RoleConstant} from "../../../../@core/constants/role.constant";

@Component({
    selector: 'departments',
    styleUrls: ['./departments.component.scss'],
    templateUrl: './departments.component.html'
})
export class DepartmentsComponent implements OnInit {
    @ViewChild('departmentsTable')
    departmentsTable: CustomTableComponent;

    departmentsColumns: Column[] = [];
    departmentsSource: DepartmentsDataSource;
    departmentsLoading: boolean = true;

    currentAccount: SimpleAccountModel;

    constructor(private departmentService: DepartmentService,
                private accountService: AccountService) {
        this.departmentsSource = new DepartmentsDataSource(this.departmentService);
        this.departmentsSource.onLoading().subscribe(load => this.departmentsLoading = load);
    }

    ngOnInit() {
        this.accountService.getCurrentAccount()
            .subscribe(account => this.currentAccount = account);

        const codeColumn: Column = new Column('code', {
            title: 'Номер',
            type: 'string'
        }, null);

        const nameColumn: Column = new Column('name', {
            title: 'Имя',
            type: 'string'
        }, null);

        const abbreviatedNameColumn: Column = new Column('abbreviatedName', {
            title: 'Сокращение',
            type: 'string'
        }, null);



        this.departmentsColumns.push(codeColumn, abbreviatedNameColumn, nameColumn);
    }

    departmentsEdit(event: TableEventEditModel<DepartmentModel>) {
        const departmentRequestModel: DepartmentRequestModel = new DepartmentRequestModel();
        departmentRequestModel.abbreviatedName = event.newData.abbreviatedName;
        departmentRequestModel.name = event.newData.name;
        departmentRequestModel.code = event.newData.code;

        this.departmentService.updateDepartment(event.data.id, departmentRequestModel)
            .subscribe((response: DepartmentModel) => event.confirm.resolve(event.newData, response),
                error2 => event.confirm.reject({}));
    }

    departmentsCreate(event: TableEventAddModel<DepartmentModel>) {
        const departmentRequestModel: DepartmentRequestModel = new DepartmentRequestModel();
        departmentRequestModel.abbreviatedName = event.newData.abbreviatedName;
        departmentRequestModel.name = event.newData.name;
        departmentRequestModel.code = event.newData.code;

        this.departmentService.createDepartment(departmentRequestModel)
            .subscribe((response: DepartmentModel) => event.confirm.resolve(response),
                error2 => event.confirm.reject({}));
    }

    departmentsDelete(event: TableEventDeleteModel<DepartmentModel>) {
        if (confirm(`Вы действительно хотите удалить: ${event.data.abbreviatedName}`)) {
            this.departmentService.deleteDepartment(event.data.id)
                .subscribe(response => event.confirm.resolve({}),
                    error2 => event.confirm.reject({}));
        }
    }

    canWriteDepartment(): boolean {
        return this.currentAccount.access.includes(RoleConstant.DEPARTMENTS_WRITE);
    }
}

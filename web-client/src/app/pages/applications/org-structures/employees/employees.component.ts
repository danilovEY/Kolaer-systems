import {Component, OnInit, ViewChild} from '@angular/core';
import {Column} from 'ng2-smart-table/lib/data-set/column';
import {CustomTableComponent} from '../../../../@theme/components';
import {EmployeesDataSource} from './employees.data-source';
import {EmployeeService} from '../../../../@core/services/employee.service';
import {TableEventAddModel} from '../../../../@theme/components/table/table-event-add.model';
import {EmployeeModel} from '../../../../@core/models/employee.model';
import {TableEventDeleteModel} from '../../../../@theme/components/table/table-event-delete.model';
import {TableEventEditModel} from '../../../../@theme/components/table/table-event-edit.model';
import {Cell} from 'ng2-smart-table';
import {Category} from '../../../../@core/models/category.enum';
import {Utils} from '../../../../@core/utils/utils';

@Component({
    selector: 'employees',
    styleUrls: ['./employees.component.scss'],
    templateUrl: './employees.component.html'
})
export class EmployeesComponent implements OnInit {
    @ViewChild('employeeTable')
    employeeTable: CustomTableComponent;

    employeesColumns: Column[] = [];
    employeesSource: EmployeesDataSource;
    employeesLoading: boolean = true;

    constructor(private employeeService: EmployeeService) {
        this.employeesSource = new EmployeesDataSource(this.employeeService);
        this.employeesSource.onLoading().subscribe(load => this.employeesLoading = load);
    }

    ngOnInit() {
        const personnelNumberColumn: Column = new Column('personnelNumber', {
            title: 'Имя',
            type: 'number',
            editable: false
        }, null);

        const firstNameColumn: Column = new Column('firstName', {
            title: 'Имя',
            type: 'string'
        }, null);

        const secondNameColumn: Column = new Column('secondName', {
            title: 'Фамилия',
            type: 'string'
        }, null);

        const thirdNameColumn: Column = new Column('thirdName', {
            title: 'Отчество',
            type: 'string'
        }, null);

        const postColumn: Column = new Column('post', {
            title: 'Должность',
            type: 'string',
            valuePrepareFunction(a: any, value: EmployeeModel, cell: Cell) {
                return value.post.abbreviatedName;
            }
        }, null);

        const departmentColumn: Column = new Column('department', {
            title: 'Подразделение',
            type: 'string',
            valuePrepareFunction(a: any, value: EmployeeModel, cell: Cell) {
                return value.department.abbreviatedName;
            }
        }, null);

        const birthdayColumn: Column = new Column('birthday', {
            title: 'День рождения',
            type: 'string',
            valuePrepareFunction(a: any, value: EmployeeModel, cell: Cell) {
                return Utils.getDateFormat(value.birthday);
            }
        }, null);

        // const genderColumn: Column = new Column('gender', {
        //     title: 'Пол',
        //     type: 'string',
        //     valuePrepareFunction(a: any, value: EmployeeModel, cell: Cell) {
        //         return Gender[value.gender];
        //     }
        // }, null);

        const categoryColumn: Column = new Column('category', {
            title: 'Категория',
            type: 'string',
            valuePrepareFunction(a: any, value: EmployeeModel, cell: Cell) {
                return Category[value.category];
            }
        }, null);

        const workPhoneNumberColumn: Column = new Column('workPhoneNumber', {
            title: 'Рабочий телефон',
            type: 'string'
        }, null);

        // const homePhoneNumberColumn: Column = new Column('homePhoneNumber', {
        //     title: 'Домашний телефон',
        //     type: 'string'
        // }, null);

        // const emailColumn: Column = new Column('email', {
        //     title: 'Email',
        //     type: 'string'
        // }, null);

        // const employmentDateColumn: Column = new Column('employmentDate', {
        //     title: 'Дата устройства',
        //     type: 'string',
        //     valuePrepareFunction(a: any, value: EmployeeModel, cell: Cell) {
        //         return Utils.getDateFormat(value.employmentDate);
        //     }
        // }, null);
        //
        // const dismissalDateColumn: Column = new Column('dismissalDate', {
        //     title: 'Дата увольнения',
        //     type: 'string',
        //     valuePrepareFunction(a: any, value: EmployeeModel, cell: Cell) {
        //         return Utils.getDateFormat(value.dismissalDate);
        //     }
        // }, null);

        this.employeesColumns.push(personnelNumberColumn,
            secondNameColumn,
            firstNameColumn,
            thirdNameColumn,
            postColumn,
            departmentColumn,
            // genderColumn,
            birthdayColumn,
            categoryColumn,
            workPhoneNumberColumn,
            // homePhoneNumberColumn,
            // emailColumn,
            // employmentDateColumn,
            // dismissalDateColumn
        );
    }

    employeesEdit(event: TableEventEditModel<EmployeeModel>) {

    }

    employeesCreate(event: TableEventAddModel<EmployeeModel>) {

    }

    employeesDelete(event: TableEventDeleteModel<EmployeeModel>) {

    }
}

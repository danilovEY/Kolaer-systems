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
import {Utils} from '../../../../@core/utils/utils';
import {PostEditComponent} from '../../../../@theme/components/table/post-edit.component';
import {DepartmentEditComponent} from '../../../../@theme/components/table/department-edit.component';
import {EmployeeRequestModel} from '../../../../@core/models/employee-request.model';
import {DateEditComponent} from '../../../../@theme/components/table/date-edit.component';
import {Gender} from '../../../../@core/models/gender.enum';
import {AccountService} from '../../../../@core/services/account.service';
import {TypeWorkEditComponent} from '../../../../@theme/components/table/type-work-edit.component';
import {SimpleAccountModel} from '../../../../@core/models/simple-account.model';
import {UpdateTypeWorkEmployeeRequestModel} from '../../../../@core/models/update-type-work-employee-request.model';

@Component({
    selector: 'employees',
    styleUrls: ['./employees.component.scss'],
    templateUrl: './employees.component.html'
})
export class EmployeesComponent implements OnInit {
    @ViewChild('employeeTable')
    employeeTable: CustomTableComponent;

    @ViewChild('employeeOnlyWithTypeWorkTable')
    employeeOnlyWithTypeWorkTable: CustomTableComponent;

    employeesColumns: Column[] = [];
    employeesOnlyWithTypeWorkColumns: Column[] = [];
    employeesSource: EmployeesDataSource;
    employeesLoading: boolean = true;

    currentAccount: SimpleAccountModel;

    constructor(private employeeService: EmployeeService,
                private accountService: AccountService) {
    }


    ngOnInit() {
        this.accountService.getCurrentAccount()
            .subscribe(account => {
                this.currentAccount = account;

                if (!this.currentAccount.accessOk) {
                    this.employeeService.getCurrentEmployee()
                        .subscribe(employee => {
                            this.employeesSource = new EmployeesDataSource(this.employeeService, employee.department.id);
                            this.employeesSource.onLoading().subscribe(load => this.employeesLoading = load);
                        });
                } else {
                    this.employeesSource = new EmployeesDataSource(this.employeeService);
                    this.employeesSource.onLoading().subscribe(load => this.employeesLoading = load);
                }
            });

        const initialsReadOnlyColumn: Column = new Column('initials', {
            title: 'Имя',
            type: 'string',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
        }, null);

        const postReadOnlyColumn: Column = new Column('post', {
            title: 'Должность',
            type: 'string',
            editable: false,
            addable: false,
            editor: {
                type: 'custom',
                component: PostEditComponent,
            },
            filter: false,
            sort: false,
            valuePrepareFunction(a: any, value: EmployeeModel, cell: Cell) {
                return value.post.abbreviatedName;
            }
        }, null);



        const personnelNumberColumn: Column = new Column('personnelNumber', {
            title: 'Табельный номер',
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
            editor: {
                type: 'custom',
                component: PostEditComponent,
            },
            valuePrepareFunction(a: any, value: EmployeeModel, cell: Cell) {
                return value.post.abbreviatedName;
            }
        }, null);

        const typeWorkColumn: Column = new Column('typeWork', {
            title: 'Вид работы',
            type: 'string',
            editor: {
                type: 'custom',
                component: TypeWorkEditComponent,
            },
            filter: false,
            sort: false,
            valuePrepareFunction(a: any, value: EmployeeModel, cell: Cell) {
                return value.typeWork ? value.typeWork.name : '';
            }
        }, null);

        const harmfulnessColumn: Column = new Column('harmfulness', {
            title: 'Вредность',
            type: 'boolean',
            addable: true,
            editable: true,
            editor: {
                type: 'checkbox',
                config: {
                    true: 'Да',
                    false: 'Нет',
                },
            },
            filter: false,
            sort: false,
            valuePrepareFunction(a: any, value: EmployeeModel, cell: Cell) {
                return value.harmfulness ? 'Да' : 'Нет';
            }
        }, null);

        const departmentColumn: Column = new Column('department', {
            title: 'Подразделение',
            type: 'string',
            editor: {
                type: 'custom',
                component: DepartmentEditComponent,
            },
            valuePrepareFunction(a: any, value: EmployeeModel, cell: Cell) {
                return value.department.abbreviatedName;
            }
        }, null);

        const birthdayColumn: Column = new Column('birthday', {
            title: 'День рождения',
            type: 'string',
            editor: {
                type: 'custom',
                component: DateEditComponent,
            },
            valuePrepareFunction(a: any, value: EmployeeModel, cell: Cell) {
                return Utils.getDateFormat(value.birthday);
            }
        }, null);

        const genderColumn: Column = new Column('gender', {
            title: 'Пол',
            type: 'string',
            filter: {
                type: 'list',
                config: {
                    selectText: 'Пол...',
                    list: [
                        { value: Utils.keyFromValue(Gender, Gender.UNKNOWN), title: Gender.UNKNOWN },
                        { value: Utils.keyFromValue(Gender, Gender.MALE), title: Gender.MALE },
                        { value: Utils.keyFromValue(Gender, Gender.FEMALE), title: Gender.FEMALE },
                    ],
                },
            },
            editor: {
                type: 'list',
                config: {
                    list: [
                        { value: Utils.keyFromValue(Gender, Gender.UNKNOWN), title: Gender.UNKNOWN },
                        { value: Utils.keyFromValue(Gender, Gender.MALE), title: Gender.MALE },
                        { value: Utils.keyFromValue(Gender, Gender.FEMALE), title: Gender.FEMALE },
                    ],
                },
            },
            valuePrepareFunction(a: any, value: EmployeeModel, cell: Cell) {
                return Gender[value.gender];
            }
        }, null);

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

        this.employeesOnlyWithTypeWorkColumns.push(
            initialsReadOnlyColumn,
            postReadOnlyColumn,
            typeWorkColumn,
            harmfulnessColumn);

        this.employeesColumns.push(personnelNumberColumn,
            secondNameColumn,
            firstNameColumn,
            thirdNameColumn,
            postColumn,
            departmentColumn,
            typeWorkColumn,
            harmfulnessColumn,
            genderColumn,
            birthdayColumn,
            // employmentDateColumn,
            // dismissalDateColumn
        );
    }

    employeesEdit(event: TableEventEditModel<EmployeeModel>) {
        const employeeRequestModel: EmployeeRequestModel = new EmployeeRequestModel();
        employeeRequestModel.firstName = event.newData.firstName;
        employeeRequestModel.secondName = event.newData.secondName;
        employeeRequestModel.thirdName = event.newData.thirdName;
        employeeRequestModel.birthday = Utils.getDateTimeToSend(event.newData.birthday);
        employeeRequestModel.category = event.newData.category;
        employeeRequestModel.gender = event.newData.gender;
        employeeRequestModel.personnelNumber = event.newData.personnelNumber;
        employeeRequestModel.postId = event.newData.post ? event.newData.post.id : null;
        employeeRequestModel.departmentId = event.newData.department ? event.newData.department.id : null;
        employeeRequestModel.typeWorkId = event.newData.typeWork ? event.newData.typeWork.id : null;
        employeeRequestModel.harmfulness = event.newData.harmfulness === true || String(event.newData.harmfulness) === 'Да';
        event.newData.harmfulness = employeeRequestModel.harmfulness;

        this.employeeService.updateEmployee(event.data.id, employeeRequestModel)
            .subscribe((employee: EmployeeModel) => event.confirm.resolve(event.newData, employee),
                error2 => event.confirm.reject({}));
    }


    employeesCreate(event: TableEventAddModel<EmployeeModel>) {
        const employeeRequestModel: EmployeeRequestModel = new EmployeeRequestModel();
        employeeRequestModel.firstName = event.newData.firstName;
        employeeRequestModel.secondName = event.newData.secondName;
        employeeRequestModel.thirdName = event.newData.thirdName;
        employeeRequestModel.birthday = Utils.getDateTimeToSend(event.newData.birthday);
        employeeRequestModel.category = event.newData.category;
        employeeRequestModel.gender = event.newData.gender;
        employeeRequestModel.personnelNumber = event.newData.personnelNumber;
        employeeRequestModel.postId = event.newData.post ? event.newData.post.id : null;
        employeeRequestModel.departmentId = event.newData.department ? event.newData.department.id : null;
        employeeRequestModel.typeWorkId = event.newData.typeWork ? event.newData.typeWork.id : null;
        employeeRequestModel.harmfulness = event.newData.harmfulness === true || String(event.newData.harmfulness) === 'Да';
        event.newData.harmfulness = employeeRequestModel.harmfulness;

        this.employeeService.createEmployee(employeeRequestModel)
            .subscribe((employee: EmployeeModel) => event.confirm.resolve(employee),
                error2 => event.confirm.reject({}));
    }

    employeesDelete(event: TableEventDeleteModel<EmployeeModel>) {
        if (confirm(`Вы действительно хотите удалить: ${event.data.initials}?`)) {
            this.employeeService.deleteEmployee(event.data.id)
                .subscribe(response => event.confirm.resolve(),
                    error2 => event.confirm.reject({}));
        } else {
            event.confirm.reject({});
        }
    }

    employeesOnlyWithTypeWorkEdit(event: TableEventEditModel<EmployeeModel>) {
        const request = new UpdateTypeWorkEmployeeRequestModel();
        request.typeWorkId = event.newData.typeWork ? event.newData.typeWork.id : null;
        request.harmfulness = event.newData.harmfulness === true || String(event.newData.harmfulness) === 'Да';
        event.newData.harmfulness = request.harmfulness;

        this.employeeService.updateTypeWorkEmployee(event.data.id, request)
            .subscribe((employee: EmployeeModel) => event.confirm.resolve(event.newData, employee),
                error2 => event.confirm.reject({}));
    }
}

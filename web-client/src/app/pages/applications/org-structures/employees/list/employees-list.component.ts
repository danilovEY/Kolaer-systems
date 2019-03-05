import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {Column} from 'ng2-smart-table/lib/data-set/column';
import {CustomTableComponent} from '../../../../../@theme/components/index';
import {EmployeesListDataSource} from './employees-list.data-source';
import {EmployeeService} from '../../../../../@core/services/employee.service';
import {EmployeeModel} from '../../../../../@core/models/employee.model';
import {TableEventEditModel} from '../../../../../@theme/components/table/table-event-edit.model';
import {Cell} from 'ng2-smart-table';
import {Utils} from '../../../../../@core/utils/utils';
import {PostEditComponent} from '../../../../../@theme/components/table/post-edit.component';
import {DepartmentEditComponent} from '../../../../../@theme/components/table/department-edit.component';
import {DateEditComponent} from '../../../../../@theme/components/table/date-edit.component';
import {AccountService} from '../../../../../@core/services/account.service';
import {SimpleAccountModel} from '../../../../../@core/models/simple-account.model';
import {UpdateTypeWorkEmployeeRequestModel} from '../../../../../@core/models/update-type-work-employee-request.model';
import {CustomActionEventModel} from "../../../../../@theme/components/table/custom-action-event.model";
import {CustomActionModel} from "../../../../../@theme/components/table/custom-action.model";
import {Router} from "@angular/router";
import {RouterClientConstant} from "../../../../../@core/constants/router-client.constant";
import {PathVariableConstant} from "../../../../../@core/constants/path-variable.constant";
import {RoleConstant} from "../../../../../@core/constants/role.constant";
import {Title} from "@angular/platform-browser";
import {takeUntil, tap} from "rxjs/operators";
import {Subject} from "rxjs";

@Component({
    selector: 'employees-list',
    styleUrls: ['./employees-list.component.scss'],
    templateUrl: './employees-list.component.html'
})
export class EmployeesListComponent implements OnInit, OnDestroy {
    private readonly openActionName: string = 'open';
    private readonly destroySubjects: Subject<any> = new Subject<any>();

    @ViewChild('employeeTable')
    employeeTable: CustomTableComponent;

    employeesColumns: Column[] = [];
    employeesOnlyWithTypeWorkColumns: Column[] = [];
    employeeActions: CustomActionModel[] = [];
    employeesSource: EmployeesListDataSource;
    employeesLoading: boolean = true;

    currentAccount: SimpleAccountModel;

    constructor(private employeeService: EmployeeService,
                private accountService: AccountService,
                private router: Router,
                private titleService: Title
    ) {
        this.titleService.setTitle('Список сотрудников');
    }

    ngOnDestroy() {
        this.destroySubjects.next(true);
        this.destroySubjects.complete();
    }

    ngOnInit() {
        this.accountService.getCurrentAccount()
            .pipe(
                tap((account: SimpleAccountModel) => this.currentAccount = account),
                takeUntil(this.destroySubjects)
            )
            .subscribe((account: SimpleAccountModel) => {
                if (!this.currentAccount.access.includes(RoleConstant.EMPLOYEES_READ)) {
                    this.employeeService.getCurrentEmployee()
                        .pipe(takeUntil(this.destroySubjects))
                        .subscribe((employee: EmployeeModel) => {
                            this.employeesSource = new EmployeesListDataSource(this.employeeService, employee.department.id);
                            this.employeesSource.onLoading().subscribe(load => this.employeesLoading = load);
                        });
                } else {
                    this.employeesSource = new EmployeesListDataSource(this.employeeService);
                    this.employeesSource.onLoading().subscribe(load => this.employeesLoading = load);
                }
            });

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

        this.employeesColumns.push(personnelNumberColumn,
            secondNameColumn,
            firstNameColumn,
            thirdNameColumn,
            postColumn,
            departmentColumn,
            birthdayColumn
        );

        const openAction: CustomActionModel = new CustomActionModel();
        openAction.name = this.openActionName;
        openAction.content = '<i class="fa fa-eye"></i>';
        openAction.description = 'Открыть';

        this.employeeActions.push(openAction);
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

    employeesAction(event: CustomActionEventModel<EmployeeModel>) {
        if (event.action.name === this.openActionName) {
            const url: string = Utils.createUrlFromUrlTemplate(
                RouterClientConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_COMMONS_URL,
                PathVariableConstant.EMPLOYEE_ID,
                event.data.id.toString()
            );

            this.router.navigate([url]);
        }
    }
}

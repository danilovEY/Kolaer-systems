import {Component, OnInit, ViewChild} from '@angular/core';
import {EmployeeModel} from '../../../../@core/models/employee.model';
import {AccountService} from '../../../../@core/services/account.service';
import {EmployeeService} from '../../../../@core/services/employee.service';
import {FindEmployeeRequestModel} from '../../../../@core/models/find-employee-request.model';
import {DepartmentModel} from '../../../../@core/models/department.model';
import {DepartmentService} from '../../../../@core/services/department.service';
import {SimpleAccountModel} from '../../../../@core/models/simple-account.model';
import {DepartmentSortModel} from '../../../../@core/models/department-sort.model';
import {SortTypeEnum} from '../../../../@core/models/sort-type.enum';
import {DepartmentFilterModel} from '../../../../@core/models/department-filter.model';
import {VacationSetDataSource} from './vacation-set.data-source';
import {VacationService} from '../vacation.service';
import {Utils} from '../../../../@core/utils/utils';
import {Column} from 'ng2-smart-table/lib/data-set/column';
import {CustomActionModel} from '../../../../@theme/components/table/custom-action.model';
import {CustomTableComponent} from '../../../../@theme/components';
import {TableEventEditModel} from '../../../../@theme/components/table/table-event-edit.model';
import {VacationModel} from '../model/vacation.model';
import {TableEventDeleteModel} from '../../../../@theme/components/table/table-event-delete.model';
import {TableEventAddModel} from '../../../../@theme/components/table/table-event-add.model';
import {VacationDateFromEditComponent} from './vacation-date-from-edit.component';
import {VacationDateToEditComponent} from './vacation-date-to-edit.component';
import {VacationDaysEditComponent} from './vacation-days-edit.component';

@Component({
    selector: 'vacation-set',
    templateUrl: './vacation-set.component.html',
    styleUrls: ['./vacation-set.component.scss']
})
export class VacationSetComponent implements OnInit {

    @ViewChild('customTable')
    customTable: CustomTableComponent;

    currentAccount: SimpleAccountModel;

    employees: EmployeeModel[] = [];
    selectedEmployee: EmployeeModel;

    departments: DepartmentModel[] = [];
    selectedDepartment: DepartmentModel;

    actions: CustomActionModel[] = [];
    columns: Column[] = [];
    source: VacationSetDataSource;
    loadingVacation: boolean = true;


    constructor(private accountService: AccountService,
                private employeeService: EmployeeService,
                private departmentService: DepartmentService,
                private vacationService: VacationService) {
        this.source = new VacationSetDataSource(this.vacationService);
        this.source.onLoading().subscribe(load => this.loadingVacation = load);
        this.source.setYear(2018);
    }

    ngOnInit(): void {
        this.accountService.getCurrentAccount()
            .subscribe(account => {
                this.currentAccount = account;
                if (account.accessVacationAdmin) {
                    const sort = new DepartmentSortModel();
                    sort.sortAbbreviatedName = SortTypeEnum.ASC;

                    this.departmentService.getAllDepartments(sort, new DepartmentFilterModel(), 1, 1000)
                        .subscribe(depPage => this.departments = depPage.data);
                } else if (account.accessVacationDepEdit) {
                    this.employeeService.getCurrentEmployee()
                        .subscribe(employee => {
                            this.selectedDepartment = employee.department;
                        });
                }
            });

        const dateFromColumn: Column = new Column('vacationFrom', {
            title: 'Начало отпуска',
            type: 'date',
            editable: true,
            addable: true,
            filter: false,
            sort: false,
            editor: {
                type: 'custom',
                component: VacationDateFromEditComponent,
            },
            valuePrepareFunction(value: string) {
                return Utils.getDateFormatFromString(value);
            }
        }, undefined);

        const dateToColumn: Column = new Column('vacationTo', {
            title: 'Конец отпуска',
            type: 'date',
            editable: true,
            addable: true,
            filter: false,
            sort: false,
            editor: {
                type: 'custom',
                component: VacationDateToEditComponent,
            },
            valuePrepareFunction(value: string) {
                return Utils.getDateFormatFromString(value);
            }
        }, undefined);

        const daysColumn: Column = new Column('vacationDays', {
            title: 'Дней',
            type: 'number',
            editable: true,
            addable: true,
            filter: false,
            sort: false,
            editor: {
                type: 'custom',
                config: {},
                component: VacationDaysEditComponent
            }
        }, undefined);

        const noteColumn: Column = new Column('note', {
            title: 'Примечание',
            type: 'string',
            editable: true,
            addable: true,
            filter: false,
            sort: false
        }, undefined);

        this.columns.push(dateFromColumn, dateToColumn, daysColumn, noteColumn);
    }

    edit(event: TableEventEditModel<VacationModel>) {
        console.log(event);
        // this.queueService.updateQueueRequest(this.targetId, event.data.id, event.newData)
        //     .subscribe(event.confirm.resolve);
    }

    delete(event: TableEventDeleteModel<VacationModel>) {
        // this.queueService.deleteQueueRequest(this.targetId, event.data.id)
        //     .subscribe(response => event.confirm.resolve());
    }

    create(event: TableEventAddModel<VacationModel>) {
        console.log(event);
        // this.queueService.addQueueRequest(this.targetId, event.newData)
        //     .subscribe(event.confirm.resolve);
    }

    selectDepartment(event: DepartmentModel) {
        const findRequest = new FindEmployeeRequestModel();
        findRequest.departmentId = event.id;
        findRequest.onOnePage = true;

        this.employeeService.findAllEmployees(findRequest)
            .subscribe(employeePage => {
                this.employees = employeePage.data;
            });
    }

    selectEmployee(event: EmployeeModel) {
        this.selectedEmployee = event;
        this.source.setEmployeeId(event.id);
        this.source.refreshFromServer();

    }
}

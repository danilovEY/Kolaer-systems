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
import {Toast, ToasterConfig, ToasterService} from 'angular2-toaster';
import {VacationPeriodModel} from '../model/vacation-period.model';
import {VacationPeriodStatusEnum} from '../model/vacation-period-status.enum';
import {VacationPeriodService} from '../vacation-period.service';
import {Title} from '@angular/platform-browser';
import {FindVacationBalanceRequestModel} from '../model/find-vacation-balance-request.model';
import {VacationBalanceModel} from '../model/vacation-balance.model';
import {tap} from 'rxjs/internal/operators';
import {SmartTableService} from '../../../../@core/services/smart-table.service';
import {FormControl, FormGroup} from '@angular/forms';

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

    periods: VacationPeriodModel[] = [];
    selectedPeriod: VacationPeriodModel;

    actions: CustomActionModel[] = [];
    columns: Column[] = [];
    source: VacationSetDataSource;
    loadingVacation: boolean = true;

    config: ToasterConfig = new ToasterConfig({
        positionClass: 'toast-top-right',
        timeout: 5000,
        newestOnTop: true,
        tapToDismiss: true,
        preventDuplicates: false,
        animation: 'fade',
        limit: 5,
    });

    formBalance: FormGroup;

    currentBalance: VacationBalanceModel;

    constructor(private accountService: AccountService,
                private employeeService: EmployeeService,
                private departmentService: DepartmentService,
                private vacationService: VacationService,
                private toasterService: ToasterService,
                private vacationPeriodService: VacationPeriodService,
                private smartTableService: SmartTableService,
                private titleService: Title) {
        this.titleService.setTitle('Задать отпуск');

        this.source = new VacationSetDataSource(this.vacationService);
        this.source.onLoading().subscribe(load => this.loadingVacation = load);
    }

    ngOnInit(): void {
        this.formBalance = new FormGroup({
            prevYearBalance: new FormControl({value: '', disabled: true}),
            currentYearBalance: new FormControl({value: '', disabled: true}),
            nextYearBalance: new FormControl({value: '', disabled: true})
        });

        this.vacationPeriodService.selectedPeriodEvent.subscribe(period => {
            if (this.currentAccount.accessVacationAdmin || this.currentAccount.accessOit || !this.isClosed(this.selectedPeriod)) {
                this.smartTableService.addEditAction();
                this.smartTableService.addDeleteAction();
            } else {
                this.smartTableService.removeEditAction();
                this.smartTableService.removeDeleteAction();
            }
        });

        this.vacationService.getPeriods()
            .subscribe(periods => {
                this.periods = periods.data;
            });

        this.accountService.getCurrentAccount()
            .subscribe(account => {
                this.currentAccount = account;

                if (account.accessVacationAdmin) {
                    this.formBalance.controls['prevYearBalance'].enable();
                    this.formBalance.controls['currentYearBalance'].enable();
                    this.formBalance.controls['nextYearBalance'].enable();

                    const sort = new DepartmentSortModel();
                    sort.sortAbbreviatedName = SortTypeEnum.ASC;

                    this.departmentService.getAllDepartments(sort, new DepartmentFilterModel(), 1, 1000)
                        .subscribe(depPage => this.departments = depPage.data);
                } else if (account.accessVacationDepEdit) {
                    this.employeeService.getCurrentEmployee()
                        .subscribe(employee => {
                            this.selectedDepartment = employee.department;
                            this.selectDepartment(this.selectedDepartment);
                        });
                }
            });

        const dateFromColumn = new Column('vacationFrom', {
            title: 'Начало отпуска',
            type: 'date',
            editable: true,
            addable: true,
            filter: false,
            sort: false,
            editor: {
                type: 'custom',
                config: {},
                component: VacationDateFromEditComponent,
            },
            valuePrepareFunction(value: string) {
                return Utils.getDateFormatFromString(value);
            }
        }, undefined);

        const dateToColumn = new Column('vacationTo', {
            title: 'Конец отпуска',
            type: 'date',
            editable: true,
            addable: true,
            filter: false,
            sort: false,
            editor: {
                type: 'custom',
                config: {},
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
        event.newData.employeeId = this.selectedEmployee.id;

        this.vacationService.updateVacation(event.data.id, event.newData)
            .pipe(tap(vacation => this.updateBalance()))
            .subscribe(
                vacation => event.confirm.resolve(event.newData, vacation),
                responseError => {
                    const toast: Toast = {
                        type: 'error',
                        title: 'Ошибка в операции',
                        body: responseError.error.message,
                    };

                    this.toasterService.popAsync(toast);
                });
    }

    delete(event: TableEventDeleteModel<VacationModel>) {
        this.vacationService.deleteVacation(event.data.id)
            .pipe(tap(vacation => this.updateBalance()))
            .subscribe(
                responce => event.confirm.resolve(),
                responseError => {
                    const toast: Toast = {
                        type: 'error',
                        title: 'Ошибка в операции',
                        body: responseError.error.message,
                    };

                    this.toasterService.popAsync(toast);
                });
    }

    create(event: TableEventAddModel<VacationModel>) {
        event.newData.employeeId = this.selectedEmployee.id;

        this.vacationService.addVacation(event.newData)
            .pipe(tap(vacation => this.updateBalance()))
            .subscribe(
                event.confirm.resolve,
                responseError => {
                    const toast: Toast = {
                        type: 'error',
                        title: 'Ошибка в операции',
                        body: responseError.error.message,
                    };

                    this.toasterService.popAsync(toast);
                });
    }

    selectDepartment(event: DepartmentModel) {
        const findRequest = new FindEmployeeRequestModel();
        findRequest.departmentIds = [event.id];
        findRequest.onOnePage = true;

        this.employeeService.findAllEmployees(findRequest)
            .subscribe(employeePage => {
                this.employees = employeePage.data;
            });
    }

    selectEmployee(event: EmployeeModel) {
        this.selectedEmployee = event;

        this.updateBalance();

        this.source.setEmployeeId(event.id);
        this.source.refreshFromServer();
    }

    private updateBalance(): void {
        if (this.selectedEmployee) {
            const request = new FindVacationBalanceRequestModel();
            request.employeeId = this.selectedEmployee.id;

            this.vacationService.getVacationBalance(request)
                .pipe(tap(balance => {
                    this.formBalance.controls['prevYearBalance'].setValue(balance.prevYearBalance);
                    this.formBalance.controls['currentYearBalance'].setValue(balance.currentYearBalance);
                    this.formBalance.controls['nextYearBalance'].setValue(balance.nextYearBalance);
                })).subscribe(balance => this.currentBalance = balance,
                    responseError => {
                        const toast: Toast = {
                            type: 'error',
                            title: 'Ошибка в операции',
                            body: responseError.error.message,
                        };

                        this.toasterService.popAsync(toast);
                    });
        }
    }

    selectPeriod(event: VacationPeriodModel) {
        this.selectedPeriod = event;
        this.source.setYear(event.year);
        this.source.refreshFromServer();

        this.vacationPeriodService
            .selectedPeriodEvent
            .emit(this.selectedPeriod);
    }

    isClosed(selectedPeriod: VacationPeriodModel): boolean {
        return selectedPeriod
            ? selectedPeriod.status === VacationPeriodStatusEnum.CLOSE
            : false
    }

    submitBalanceForm() {
        this.currentBalance.prevYearBalance = this.formBalance.value.prevYearBalance;
        this.currentBalance.currentYearBalance = this.formBalance.value.currentYearBalance;
        this.currentBalance.nextYearBalance = this.formBalance.value.nextYearBalance;

        this.vacationService.updateVacationBalance(this.currentBalance)
            .subscribe(balance => {
                this.currentBalance = balance;

                const toast: Toast = {
                    type: 'success',
                    title: 'Успех',
                    body: 'Остатки обновились',
                };

                this.toasterService.popAsync(toast);
            }, responseError => {
                const toast: Toast = {
                    type: 'error',
                    title: 'Ошибка в операции',
                    body: responseError.error.message,
                };
                this.toasterService.popAsync(toast);
            });
    }
}

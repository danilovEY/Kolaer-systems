import {Component, OnInit} from '@angular/core';
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

@Component({
    selector: 'vacation-set',
    templateUrl: './vacation-set.component.html',
    styleUrls: ['./vacation-set.component.scss']
})
export class VacationSetComponent implements OnInit {
    currentAccount: SimpleAccountModel;

    employees: EmployeeModel[] = [];
    selectedEmployee: EmployeeModel;

    departments: DepartmentModel[] = [];
    selectedDepartment: DepartmentModel;

    // source: LocalDataSource = new LocalDataSource();
    loadingVacation: boolean = false;


    constructor(private accountService: AccountService,
                private employeeService: EmployeeService,
                private departmentService: DepartmentService) {
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
    }


    // edit(event: TableEventEditModel<>) {
    //     // this.queueService.updateQueueRequest(this.targetId, event.data.id, event.newData)
    //     //     .subscribe(event.confirm.resolve);
    // }
    //
    // delete(event: TableEventDeleteModel<>) {
    //     // this.queueService.deleteQueueRequest(this.targetId, event.data.id)
    //     //     .subscribe(response => event.confirm.resolve());
    // }
    //
    // create(event: TableEventAddModel<>) {
    //     // this.queueService.addQueueRequest(this.targetId, event.newData)
    //     //     .subscribe(event.confirm.resolve);
    // }

    selectDepartment(event: DepartmentModel) {
        const findRequest = new FindEmployeeRequestModel();
        findRequest.departmentId = event.id;
        findRequest.onOnePage = true;

        this.employeeService.findAllEmployees(findRequest)
            .subscribe(employeePage => {
                this.employees = employeePage.data;
            });
    }
}

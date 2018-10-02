import {CustomDataSource} from '../../../../@core/models/custom.data-source';
import {Page} from '../../../../@core/models/page.model';
import {EmployeeModel} from '../../../../@core/models/employee.model';
import {EmployeeService} from '../../../../@core/services/employee.service';
import {EmployeeSortModel} from '../../../../@core/models/employee-sort.model';
import {EmployeeFilterModel} from '../../../../@core/models/employee-filter.model';
import {FindEmployeeRequestModel} from '../../../../@core/models/find-employee-request.model';

export class EmployeesDataSource extends CustomDataSource<EmployeeModel> {

    constructor(private employeeService: EmployeeService, private departmentId?: number) {
        super();
    }

    loadElements(page: number, pageSize: number): Promise<EmployeeModel[]> {
        if (this.departmentId && this.departmentId > 0) {
            const request = new FindEmployeeRequestModel();
            request.departmentIds = [this.departmentId];
            request.number = page;
            request.pageSize = pageSize;

            return this.employeeService.findAllEmployees(request)
                .toPromise()
                .then((response: Page<EmployeeModel>) => this.setDataPage(response));
        } else {
            const employeeSortModel: EmployeeSortModel =
                this.getFilterModel(new EmployeeSortModel());

            const employeeFilterModel: EmployeeFilterModel =
                this.getSortModel(new EmployeeFilterModel());

            return this.employeeService.getAllEmployees(employeeSortModel, employeeFilterModel,
                page, pageSize)
                .toPromise()
                .then((response: Page<EmployeeModel>) => this.setDataPage(response));
        }
    }
}

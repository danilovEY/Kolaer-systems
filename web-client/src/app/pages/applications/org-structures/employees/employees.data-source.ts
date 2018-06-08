import {CustomDataSource} from '../../../../@core/models/custom.data-source';
import {Page} from '../../../../@core/models/page.model';
import {EmployeeModel} from '../../../../@core/models/employee.model';
import {EmployeeService} from '../../../../@core/services/employee.service';
import {EmployeeSortModel} from '../../../../@core/models/employee-sort.model';
import {EmployeeFilterModel} from '../../../../@core/models/employee-filter.model';

export class EmployeesDataSource extends CustomDataSource<EmployeeModel> {

    constructor(private employeeService: EmployeeService) {
        super();
    }

    loadElements(page: number, pageSize: number): Promise<EmployeeModel[]> {
        const employeeSortModel: EmployeeSortModel =
            this.getFilterModel(new EmployeeSortModel());

        const employeeFilterModel: EmployeeFilterModel =
            this.getSortModel(new EmployeeFilterModel());

        return this.employeeService.getAllEmployees(employeeSortModel, employeeFilterModel,
            page, pageSize)
            .toPromise()
            .then((response: Page<EmployeeModel>) => this.setData(response));
    }

}

import {CustomDataSource} from '../../../../../@core/models/custom.data-source';
import {Page} from '../../../../../@core/models/page.model';
import {EmployeeModel} from '../../../../../@core/models/employee.model';
import {EmployeeService} from '../../../../../@core/services/employee.service';
import {FindEmployeeRequestModel} from '../../../../../@core/models/find-employee-request.model';

export class EmployeesListDataSource extends CustomDataSource<EmployeeModel> {

    constructor(private employeeService: EmployeeService, private departmentId?: number) {
        super();
    }

    loadElements(page: number, pageSize: number): Promise<EmployeeModel[]> {
        const request = new FindEmployeeRequestModel();
        request.pageNum = page;
        request.pageSize = pageSize;

        if (this.departmentId && this.departmentId > 0) {
            request.departmentIds = [this.departmentId];
        }

        return this.employeeService.findAllEmployees(request)
            .toPromise()
            .then((response: Page<EmployeeModel>) => this.setDataPage(response));
    }

}


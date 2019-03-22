import {CustomDataSource} from '../../../../../@core/models/custom.data-source';
import {Page} from '../../../../../@core/models/page.model';
import {EmployeeModel} from '../../../../../@core/models/employee.model';
import {EmployeeService} from '../../../../../@core/services/employee.service';
import {FindEmployeeRequestModel} from '../../../../../@core/models/employee/request/find-employee-request.model';
import {TableFilters} from "../../../../../@theme/components/table/table-filter";
import {ColumnSort} from "../../../../../@theme/components/table/column-sort";
import {EmployeeSortTypeEnum} from "../../../../../@core/models/employee/request/employee-sort-type.enum";

export class EmployeesListDataSource extends CustomDataSource<EmployeeModel> {
    public static INITIALS_COLUMN_KEY = 'initials';
    public static PERSONNEL_NUMBER_COLUMN_KEY = 'personnelNumber';
    public static POST_COLUMN_KEY = 'post';
    public static DEPARTMENT_COLUMN_KEY = 'department';
    public static BIRTHDAY_COLUMN_KEY = 'birthday';

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

        const tableFilters: TableFilters = this.getFilter();

        for (const filter of tableFilters.filters.filter(f => f.search !== '')) {
            if (filter.field === EmployeesListDataSource.INITIALS_COLUMN_KEY) {
                request.findByInitials = filter.search;
            } else if (filter.field === EmployeesListDataSource.PERSONNEL_NUMBER_COLUMN_KEY && !isNaN(Number(filter.search))) {
                request.findByPersonnelNumber = Number(filter.search);
            } else if (filter.field === EmployeesListDataSource.POST_COLUMN_KEY) {
                request.findByPostName = filter.search;
            } else if (filter.field === EmployeesListDataSource.DEPARTMENT_COLUMN_KEY) {
                request.findByDepartmentName = filter.search;
            }
        }

        request.sorts = this.getSort().map(c => this.mapToSort(c));

        return this.employeeService.findAllEmployees(request)
            .toPromise()
            .then((response: Page<EmployeeModel>) => this.setDataPage(response));
    }

    private mapToSort(columnSort: ColumnSort): EmployeeSortTypeEnum {
        switch (columnSort.field) {
            case EmployeesListDataSource.INITIALS_COLUMN_KEY: return columnSort.direction === 'asc'
                ? EmployeeSortTypeEnum.INITIALS_ASC
                : EmployeeSortTypeEnum.INITIALS_DESC;

            case EmployeesListDataSource.PERSONNEL_NUMBER_COLUMN_KEY: return columnSort.direction === 'asc'
                ? EmployeeSortTypeEnum.PERSONNEL_NUMBER_ASC
                : EmployeeSortTypeEnum.PERSONNEL_NUMBER_DESC;
        }
    }
}


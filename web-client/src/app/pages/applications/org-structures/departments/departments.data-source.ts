import {CustomDataSource} from '../../../../@core/models/custom.data-source';
import {Page} from '../../../../@core/models/page.model';
import {DepartmentModel} from '../../../../@core/models/department.model';
import {DepartmentService} from '../../../../@core/services/department.service';
import {DepartmentSortModel} from '../../../../@core/models/department-sort.model';
import {DepartmentFilterModel} from '../../../../@core/models/department-filter.model';

export class DepartmentsDataSource extends CustomDataSource<DepartmentModel> {

    constructor(private departmentService: DepartmentService) {
        super();
    }

    loadElements(page: number, pageSize: number): Promise<DepartmentModel[]> {
        const departmentSortModel: DepartmentSortModel =
            this.getFilterModel(new DepartmentSortModel());

        const departmentFilterModel: DepartmentFilterModel =
            this.getSortModel(new DepartmentFilterModel());

        return this.departmentService.getAllDepartments(departmentSortModel, departmentFilterModel,
            page, pageSize)
            .toPromise()
            .then((response: Page<DepartmentModel>) => this.setDataPage(response));
    }

}

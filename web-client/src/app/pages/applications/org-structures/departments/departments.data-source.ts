import {CustomDataSource} from '../../../../@core/models/custom.data-source';
import {Page} from '../../../../@core/models/page.model';
import {DepartmentModel} from '../../../../@core/models/department/department.model';
import {DepartmentService} from '../../../../@core/services/department.service';
import {FindDepartmentPageRequest} from "../../../../@core/models/department/find-department-page-request";

export class DepartmentsDataSource extends CustomDataSource<DepartmentModel> {

    constructor(private departmentService: DepartmentService) {
        super();
    }

    loadElements(page: number, pageSize: number): Promise<DepartmentModel[]> {
        // const departmentSortModel: DepartmentSortModel =
        //     this.getFilterModel(new DepartmentSortModel());
        //
        // const departmentFilterModel: DepartmentFilterModel =
        //     this.getSortModel(new DepartmentFilterModel());

        return this.departmentService.find(FindDepartmentPageRequest.findRequest(page, pageSize))
            .toPromise()
            .then((response: Page<DepartmentModel>) => this.setDataPage(response));
    }

}

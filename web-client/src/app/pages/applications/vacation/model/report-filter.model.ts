import {DepartmentModel} from '../../../../@core/models/department/department.model';
import {EmployeeModel} from '../../../../@core/models/employee.model';
import {PostModel} from '../../../../@core/models/post.model';
import {TypeWorkModel} from '../../../../@core/models/type-work.model';

export class ReportFilterModel {
    public selectedTypeWorks: TypeWorkModel[] = [];
    public selectedPosts: PostModel[] = [];
    public selectedEmployees: EmployeeModel[] = [];
    public selectedDepartments: DepartmentModel[] = [];
    // public selectedDepartment: DepartmentModel;
    public addOtherData: boolean = false;
    public groupByDepartments: boolean = false;
    public selectedAllDepartments: boolean = false;
    public pipeCharts: boolean = false;
    public calculateIntersections: boolean = true;
    public from: Date;
    public to: Date;
}

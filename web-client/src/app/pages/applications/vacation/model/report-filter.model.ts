import {DepartmentModel} from '../../../../@core/models/department.model';

export class ReportFilterModel {
    public selectedDepartments: DepartmentModel[] = [];
    public selectedDepartment: DepartmentModel;
    public selectedAllDepartments: boolean = false;
    public pipeCharts: boolean = false;
    public calculateIntersections: boolean = true;
    public from: Date;
    public to: Date;
}

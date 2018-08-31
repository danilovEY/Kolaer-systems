import {DepartmentModel} from '../../../../@core/models/department.model';

export class ReportFilterModel {
    public selectedDepartments: DepartmentModel[] = [];
    public selectedAllDepartments: boolean = false;
    public pipeCharts: boolean = false;
    public from: Date;
    public to: Date;
}

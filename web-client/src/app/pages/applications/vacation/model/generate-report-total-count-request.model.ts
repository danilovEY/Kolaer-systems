export class GenerateReportTotalCountRequestModel {
    public departmentIds: number[];
    public employeeIds: number[];
    public postIds: number[];
    public typeWorkIds: number[];
    public groupByDepartments: boolean = false;
    public from: Date;
    public to: Date;
}

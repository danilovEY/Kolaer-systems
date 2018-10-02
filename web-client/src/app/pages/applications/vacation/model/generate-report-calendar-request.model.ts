export class GenerateReportCalendarRequestModel {
    public departmentIds: number[];
    public employeeIds: number[];
    public postIds: number[];
    public typeWorkIds: number[];
    public allDepartment: boolean = false;
    public from: Date;
    public to: Date;
}

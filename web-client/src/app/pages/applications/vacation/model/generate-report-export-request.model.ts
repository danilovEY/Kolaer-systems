export class GenerateReportExportRequestModel {
    public departmentId: number;
    public employeeIds: number[];
    public postIds: number[];
    public typeWorkIds: number[];
    public from: Date;
    public to: Date;
}

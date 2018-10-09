export class GenerateReportDistributeRequestModel {
    public departmentIds: number[];
    public employeeIds: number[];
    public postIds: number[];
    public typeWorkIds: number[];
    public addOtherData: boolean = false;
    public addPipesForVacation: boolean = false;
    public groupByDepartments: boolean = false;
    public calculateIntersections: boolean = false;
    public from: Date;
    public to: Date;
}

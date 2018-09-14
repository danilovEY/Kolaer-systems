export class GenerateReportDistributeRequestModel {
    public departmentIds: number[];
    public allDepartment: boolean = false;
    public addPipesForVacation: boolean = false;
    public calculateIntersections: boolean = false;
    public from: Date;
    public to: Date;
}

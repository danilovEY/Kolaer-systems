import {CustomDataSource} from "../../../../../../@core/models/custom.data-source";
import {EmploymentHistoryModel} from "../../../../../../@core/models/employee/employment-history.model";

export class EmployeeCardEmploymentHistoriesDataSource extends CustomDataSource<EmploymentHistoryModel> {
    private readonly fakeList: EmploymentHistoryModel[] = [];

    constructor() {
        super();

        const emp: EmploymentHistoryModel = new EmploymentHistoryModel();
        emp.organization = 'КолАЭР';
        emp.post = 'Инженер';
        emp.startWorkDate = new Date();

        this.fakeList.push(emp)
    }

    loadElements(page: number, pageSize: number): Promise<EmploymentHistoryModel[]> {
        return new Promise<EmploymentHistoryModel[]>(resolve => resolve(this.fakeList));
    }


}

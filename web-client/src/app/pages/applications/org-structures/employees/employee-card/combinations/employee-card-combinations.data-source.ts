import {CustomDataSource} from "../../../../../../@core/models/custom.data-source";
import {EmployeeCombinationModel} from "../../../../../../@core/models/employee/employee-combination.model";

export class EmployeeCardCombinationsDataSource extends CustomDataSource<EmployeeCombinationModel> {
    private readonly fakeList: EmployeeCombinationModel[] = [];

    constructor() {
        super();

        const emp: EmployeeCombinationModel = new EmployeeCombinationModel();
        emp.post = 'Старший мастер';
        emp.startDate = new Date();
        emp.endDate = new Date();

        this.fakeList.push(emp)
    }

    loadElements(page: number, pageSize: number): Promise<EmployeeCombinationModel[]> {
        return new Promise<EmployeeCombinationModel[]>(resolve => resolve(this.fakeList));
    }


}

import {CustomDataSource} from "../../../../../../@core/models/custom.data-source";
import {EmployeeRelativeModel} from "../../../../../../@core/models/employee/employee-relative.model";

export class EmployeeCardRelativesDataSource extends CustomDataSource<EmployeeRelativeModel> {
    private readonly fakeList: EmployeeRelativeModel[] = [];

    constructor() {
        super();

        const emp: EmployeeRelativeModel = new EmployeeRelativeModel();
        emp.relationDegree = 'Сын';
        emp.initials = 'Иванов Андрей Иванович';
        emp.dateOfBirth = new Date();

        this.fakeList.push(emp);
    }

    loadElements(page: number, pageSize: number): Promise<EmployeeRelativeModel[]> {
        return new Promise<EmployeeRelativeModel[]>(resolve => resolve(this.fakeList));
    }

}

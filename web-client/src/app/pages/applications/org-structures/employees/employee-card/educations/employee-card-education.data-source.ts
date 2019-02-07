import {CustomDataSource} from "../../../../../../@core/models/custom.data-source";
import {EmployeeEducationModel} from "../../../../../../@core/models/employee/employee-education.model";

export class EmployeeCardEducationDataSource extends CustomDataSource<EmployeeEducationModel> {
    private readonly fakeList: EmployeeEducationModel[] = [];

    constructor() {
        super();

        const emp: EmployeeEducationModel = new EmployeeEducationModel();
        emp.typeEducation = 'Начал. профессион.';
        emp.institution = 'Пту-18 г. Полярные Зори Мурманской Области';
        emp.specialty = 'Машинист бульдозера';
        emp.qualification = 'машинист бульдозера четвертого разряда';
        emp.document = 'Диплом';
        emp.documentNumber = '922152';
        emp.expirationDate = new Date();

        this.fakeList.push(emp)
    }

    loadElements(page: number, pageSize: number): Promise<EmployeeEducationModel[]> {
        return new Promise<EmployeeEducationModel[]>(resolve => resolve(this.fakeList));
    }


}

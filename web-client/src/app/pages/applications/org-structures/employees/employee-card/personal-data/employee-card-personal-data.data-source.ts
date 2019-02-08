import {CustomDataSource} from "../../../../../../@core/models/custom.data-source";
import {EmployeePersonalDataModel} from "../../../../../../@core/models/employee/employee-personal-data.model";

export class EmployeeCardPersonalDataDataSource extends CustomDataSource<EmployeePersonalDataModel> {
    private readonly fakeList: EmployeePersonalDataModel[] = [];

    constructor() {
        super();

        const emp: EmployeePersonalDataModel = new EmployeePersonalDataModel();
        emp.maritalStatus = 'Жен/ЗМ';
        emp.phoneNumber = '8-999-999-99-99';
        emp.addressRegistration = '1111111, г. Мурманск';
        emp.addressResidential = '1111111, г. Полярные Зори';
        emp.placeOfBirth = 'г. Пермь';

        this.fakeList.push(emp)
    }

    loadElements(page: number, pageSize: number): Promise<EmployeePersonalDataModel[]> {
        return new Promise<EmployeePersonalDataModel[]>(resolve => resolve(this.fakeList));
    }


}

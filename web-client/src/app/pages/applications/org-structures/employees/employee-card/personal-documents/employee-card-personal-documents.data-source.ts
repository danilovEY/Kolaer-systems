import {CustomDataSource} from "../../../../../../@core/models/custom.data-source";
import {EmployeePersonalDocumentModel} from "../../../../../../@core/models/employee/employee-personal-document.model";

export class EmployeeCardPersonalDocumentsDataSource extends CustomDataSource<EmployeePersonalDocumentModel> {
    private readonly fakeList: EmployeePersonalDocumentModel[] = [];

    constructor() {
        super();

        const emp: EmployeePersonalDocumentModel = new EmployeePersonalDocumentModel();
        emp.name = 'Военный билет';
        emp.dateOfIssue = new Date();
        emp.documentNumber = '222844';
        emp.issuedBy = 'Комирасом';

        this.fakeList.push(emp)
    }

    loadElements(page: number, pageSize: number): Promise<EmployeePersonalDocumentModel[]> {
        return new Promise<EmployeePersonalDocumentModel[]>(resolve => resolve(this.fakeList));
    }


}

import {CustomDataSource} from "../../../../../../@core/models/custom.data-source";
import {EmployeePersonalDocumentModel} from "../../../../../../@core/models/employee/employee-personal-document.model";
import {EmployeePersonalDocumentService} from "./employee-personal-document.service";
import {EmployeeCardService} from "../employee-card.service";

export class EmployeeCardPersonalDocumentsDataSource extends CustomDataSource<EmployeePersonalDocumentModel> {

    constructor(private employeeCardService: EmployeeCardService,
                private employeePersonalDocumentService: EmployeePersonalDocumentService) {
        super();
    }

    loadElements(page: number, pageSize: number): Promise<EmployeePersonalDocumentModel[]> {
        return this.employeePersonalDocumentService
            .getPersonalDocumentsByEmployeeId(this.employeeCardService.selectedEmployeeId.getValue())
            .toPromise()
            .then((personalDocuments: EmployeePersonalDocumentModel[]) => this.setData(personalDocuments));
    }


}

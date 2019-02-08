import {CustomDataSource} from "../../../../../../@core/models/custom.data-source";
import {VacationModel} from "../../../../vacation/model/vacation.model";
import {VacationTypeEnum} from "../../../../vacation/model/vacation-type-enum.model";

export class EmployeeCardVacationsDataSource extends CustomDataSource<VacationModel> {
    private readonly fakeList: VacationModel[] = [];

    constructor() {
        super();

        const emp: VacationModel = new VacationModel();
        emp.note = 'Нужный отпуск';
        emp.vacationFrom = new Date();
        emp.vacationTo = new Date();
        emp.vacationDays = 1;
        emp.vacationType = VacationTypeEnum.PAID_HOLIDAY;

        this.fakeList.push(emp)
    }

    loadElements(page: number, pageSize: number): Promise<VacationModel[]> {
        return new Promise<VacationModel[]>(resolve => resolve(this.fakeList));
    }


}

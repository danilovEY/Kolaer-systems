import {CustomDataSource} from '../../../../@core/models/custom.data-source';
import {VacationModel} from '../model/vacation.model';
import {VacationService} from '../vacation.service';
import {FindVacationRequestModel} from '../model/find-vacation-request.model';
import {Page} from '../../../../@core/models/page.model';

export class VacationSetDataSource extends CustomDataSource<VacationModel> {
    private employeeId: number;
    private year: number;

    constructor(private vacationService: VacationService) {
        super();
    }

    loadElements(page: number, pageSize: number): Promise<VacationModel[]> {
        const request = new FindVacationRequestModel();
        request.employeeId = this.employeeId;
        request.year = this.year;

        return this.vacationService.getVacations(request)
            .toPromise()
            .then((response: Page<VacationModel>) => this.setDataPage(response));
    }

    setEmployeeId(employeeId: number): void {
        this.employeeId = employeeId;
    }

    setYear(year: number): void {
        this.year = year;
    }

}

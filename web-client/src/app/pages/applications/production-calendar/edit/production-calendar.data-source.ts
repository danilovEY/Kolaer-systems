import {CustomDataSource} from '../../../../@core/models/custom.data-source';
import {HolidayModel} from '../../../../@core/models/holiday.model';
import {ProductionCalendarService} from '../../../../@core/services/production-calendar.service';
import {HolidaySortModel} from '../../../../@core/models/holiday-sort.model';
import {HolidayFilterModel} from '../../../../@core/models/holiday-filter.model';
import {Page} from '../../../../@core/models/page.model';

export class ProductionCalendarDataSource extends CustomDataSource<HolidayModel> {

    constructor(private productionCalendarService: ProductionCalendarService) {
        super();
    }

    loadElements(page: number, pageSize: number): Promise<HolidayModel[]> {
        const holidaySortModel: HolidaySortModel =
            this.getFilterModel(new HolidaySortModel());

        const holidayFilterModel: HolidayFilterModel =
            this.getSortModel(new HolidayFilterModel());

        return this.productionCalendarService.getAllHoliday(holidaySortModel, holidayFilterModel, page, pageSize)
            .toPromise()
            .then((response: Page<HolidayModel>) => this.setDataPage(response));
    }
}

import {BaseService} from './base.service';
import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import {HolidayModel} from '../models/holiday.model';
import {environment} from '../../../environments/environment';
import {tap} from 'rxjs/operators';
import {Page} from '../models/page.model';
import {HolidaySortModel} from '../models/holiday-sort.model';
import {HolidayFilterModel} from '../models/holiday-filter.model';

@Injectable()
export class ProductionCalendarService extends BaseService {
    private readonly getAllHistory = `${environment.publicServerUrl}/non-security/holidays`;

    constructor(private http: HttpClient) {
        super();
    }


    createHoliday(model: HolidayModel): Observable<HolidayModel> {
        return this.http.post<HolidayModel>(this.getAllHistory, model)
            .pipe(tap(this.convertModel));
    }

    updateHoliday(id: number, model: HolidayModel): Observable<HolidayModel> {
        const url = `${this.getAllHistory}/${id}`;

        return this.http.put<HolidayModel>(url, model)
            .pipe(tap(this.convertModel));
    }

    deleteHoliday(id: number): Observable<any> {
        const url = `${this.getAllHistory}/${id}`;

        return this.http.delete<HolidayModel>(url);
    }

    getAllHoliday(sort?: HolidaySortModel, filter?: HolidayFilterModel,
                  page: number = 1, pageSize: number = 15): Observable<Page<HolidayModel>> {
        let params = new HttpParams();

        params = params.append('page', page.toString()).append('pagesize', pageSize.toString());
        params = this.getSortAndFilterParam(params, sort, filter);

        return this.http.get<Page<HolidayModel>>(this.getAllHistory, {params})
            .pipe(
                tap((response: Page<HolidayModel>) => response.data.map(this.convertModel))
            );
    }

    getAllHolidayByYear(year?: number): Observable<HolidayModel[]> {
        const url = `${this.getAllHistory}/get/${year ? year : new Date().getFullYear()}`;

        return this.http.get<HolidayModel[]>(url)
            .pipe(
                tap((response: HolidayModel[]) => response.map(this.convertModel))
            );
    }


    convertModel(model: HolidayModel): HolidayModel {
        model.holidayDate = model.holidayDate ? new Date(model.holidayDate) : null;

        return model;
    }
}

import {Injectable} from '@angular/core';
import {BaseService} from '../../../@core/services/base.service';
import {HttpClient, HttpParams} from '@angular/common/http';
import {FindVacationRequestModel} from './model/find-vacation-request.model';
import {Observable} from 'rxjs/Rx';
import {VacationModel} from './model/vacation.model';
import {Page} from '../../../@core/models/page.model';
import {environment} from '../../../../environments/environment';
import {VacationCalculateModel} from './model/vacation-calculate.model';
import {VacationCalculateDaysRequestModel} from './model/vacation-calculate-days-request.model';
import {VacationCalculateDateRequestModel} from './model/vacation-calculate-date-request.model';
import {VacationPeriodModel} from './model/vacation-period.model';

@Injectable()
export class VacationService extends BaseService {
    private readonly VACATION_URL = `${environment.publicServerUrl}/vacations`;
    private readonly FIND_VACATION_URL = `${this.VACATION_URL}`;
    private readonly FIND_PERIODS_URL = `${this.VACATION_URL}/periods`;
    private readonly CALCULATE_DAYS_VACATION_URL = `${this.VACATION_URL}/calculate/days`;
    private readonly CALCULATE_DATE_VACATION_URL = `${this.VACATION_URL}/calculate/date`;

    constructor(private http: HttpClient) {
        super();
    }

    public getVacations(request: FindVacationRequestModel): Observable<Page<VacationModel>> {
        const params: HttpParams = new HttpParams()
            .append('employeeId', request.employeeId.toString())
            .append('year', request.year.toString())
            .append('number', request.number.toString())
            .append('pagesize', request.pageSize.toString());
        
        return this.http.get<Page<VacationModel>>(this.FIND_VACATION_URL, {params: params});
    }

    public getPeriods(): Observable<Page<VacationPeriodModel>> {
        return this.http.get<Page<VacationPeriodModel>>(this.FIND_PERIODS_URL);
    }

    public addVacation(request: VacationModel): Observable<VacationModel> {
        return this.http.post<VacationModel>(this.FIND_VACATION_URL, request);
    }

    public updateVacation(vacationId: number, request: VacationModel): Observable<VacationModel> {
        const url = `${this.VACATION_URL}/${vacationId}`;

        return this.http.put<VacationModel>(url, request);
    }

    public deleteVacation(vacationId: number): Observable<Object> {
        const url = `${this.VACATION_URL}/${vacationId}`;

        return this.http.delete(url);
    }

    public calculateDays(request: VacationCalculateDaysRequestModel): Observable<VacationCalculateModel> {
        return this.http.post<VacationCalculateModel>(this.CALCULATE_DAYS_VACATION_URL, request);
    }

    public calculateDate(request: VacationCalculateDateRequestModel): Observable<VacationCalculateModel> {
        return this.http.post<VacationCalculateModel>(this.CALCULATE_DATE_VACATION_URL, request);
    }

}

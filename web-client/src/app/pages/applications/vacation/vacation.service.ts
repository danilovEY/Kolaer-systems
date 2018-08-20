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

@Injectable()
export class VacationService extends BaseService {
    private readonly VACATION_URL = `${environment.publicServerUrl}/vacations`;
    private readonly FIND_VACATION_URL = `${this.VACATION_URL}`;
    private readonly CALCULATE_DAYS_VACATION_URL = `${this.VACATION_URL}/calculate/days`;

    constructor(private http: HttpClient) {
        super();
    }

    public getVacations(request: FindVacationRequestModel): Observable<Page<VacationModel>> {
        let params: HttpParams = new HttpParams();
        params = params.append('employeeId', request.employeeId.toString())
            .append('year', request.year.toString())
            .append('number', request.number.toString())
            .append('pagesize', request.pageSize.toString());
        
        return this.http.get<Page<VacationModel>>(this.FIND_VACATION_URL, {params: params});
    }

    public calculateDays(request: VacationCalculateDaysRequestModel): Observable<VacationCalculateModel> {
        return this.http.post<VacationCalculateModel>(this.CALCULATE_DAYS_VACATION_URL, request);
    }

}

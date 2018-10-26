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
import {GenerateReportCalendarRequestModel} from './model/generate-report-calendar-request.model';
import {Utils} from '../../../@core/utils/utils';
import {VacationReportDistributeModel} from './model/vacation-report-distribute.model';
import {GenerateReportDistributeRequestModel} from './model/generate-report-distribute-request.model';
import {VacationBalanceModel} from './model/vacation-balance.model';
import {FindVacationBalanceRequestModel} from './model/find-vacation-balance-request.model';
import {GenerateReportExportRequestModel} from './model/generate-report-export-request.model';
import {GenerateReportTotalCountRequestModel} from './model/generate-report-total-count-request.model';
import {VacationReportPipeModel} from './model/vacation-report-pipe.model';
import {VacationReportCalendarEmployeeModel} from './model/vacation-report-calendar-employee.model';

@Injectable()
export class VacationService extends BaseService {
    private readonly VACATION_URL = `${environment.publicServerUrl}/vacations`;
    private readonly FIND_VACATION_URL = `${this.VACATION_URL}`;
    private readonly FIND_PERIODS_URL = `${this.VACATION_URL}/periods`;
    private readonly FIND_VACATION_BALANCE_URL = `${this.VACATION_URL}/balance`;
    private readonly UPDATE_VACATION_BALANCE_URL = `${this.VACATION_URL}/balance`;
    private readonly GENERATE_REPORT_CALENDAR_EXPORT_URL = `${this.VACATION_URL}/report/calendar/export`;
    private readonly GENERATE_REPORT_CALENDAR_URL = `${this.VACATION_URL}/report/calendar`;
    private readonly GENERATE_REPORT_DISTRIBUTE_URL = `${this.VACATION_URL}/report/distribute`;
    private readonly GENERATE_REPORT_TOTAL_COUNT_URL = `${this.VACATION_URL}/report/total-count`;
    private readonly GENERATE_REPORT_EXPORT_URL = `${this.VACATION_URL}/report/export`;
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

    public getVacationBalance(request: FindVacationBalanceRequestModel): Observable<VacationBalanceModel> {
        const params: HttpParams = new HttpParams()
            .append('employeeId', request.employeeId.toString());

        return this.http.get<VacationBalanceModel>(this.FIND_VACATION_BALANCE_URL, {params: params});
    }

    public updateVacationBalance(vacationBalance: VacationBalanceModel): Observable<VacationBalanceModel> {
        return this.http.put<VacationBalanceModel>(this.UPDATE_VACATION_BALANCE_URL, vacationBalance);
    }

    public generateVacationReportCalendarAndDownload(request: GenerateReportCalendarRequestModel): Observable<any> {
        const params: HttpParams = new HttpParams()
            .append('departmentIds', request.departmentIds.toString())
            .append('employeeIds', request.employeeIds.toString())
            .append('postIds', request.postIds.toString())
            .append('typeWorkIds', request.typeWorkIds.toString())
            .append('from', Utils.getDateToSend(request.from))
            .append('to', Utils.getDateToSend(request.to));

        return this.http.get(this.GENERATE_REPORT_CALENDAR_EXPORT_URL, {
            responseType: 'blob',
            params: params
        });
    }

    public generateVacationReportCalendar(request: GenerateReportCalendarRequestModel): Observable<VacationReportCalendarEmployeeModel[]> {
        const params: HttpParams = new HttpParams()
            .append('departmentIds', request.departmentIds.toString())
            .append('employeeIds', request.employeeIds.toString())
            .append('postIds', request.postIds.toString())
            .append('typeWorkIds', request.typeWorkIds.toString())
            .append('from', Utils.getDateToSend(request.from))
            .append('to', Utils.getDateToSend(request.to));

        return this.http.get<VacationReportCalendarEmployeeModel[]>(this.GENERATE_REPORT_CALENDAR_URL, {params: params});
    }

    public generateVacationReportDistribute(request: GenerateReportDistributeRequestModel): Observable<VacationReportDistributeModel> {
        const params: HttpParams = new HttpParams()
            .append('addPipesForVacation', request.addPipesForVacation.toString())
            .append('calculateIntersections', request.calculateIntersections.toString())
            .append('departmentIds', request.departmentIds.toString())
            .append('employeeIds', request.employeeIds.toString())
            .append('postIds', request.postIds.toString())
            .append('typeWorkIds', request.typeWorkIds.toString())
            .append('addOtherData', request.addOtherData.toString())
            .append('groupByDepartments', request.groupByDepartments.toString())
            .append('from', Utils.getDateToSend(request.from))
            .append('to', Utils.getDateToSend(request.to));

        return this.http.get<VacationReportDistributeModel>(this.GENERATE_REPORT_DISTRIBUTE_URL, {params: params});
    }

    public generateVacationReportTotalCount(request: GenerateReportTotalCountRequestModel):
    Observable<VacationReportPipeModel[]> {
        const params: HttpParams = new HttpParams()
            .append('departmentIds', request.departmentIds.toString())
            .append('employeeIds', request.employeeIds.toString())
            .append('postIds', request.postIds.toString())
            .append('typeWorkIds', request.typeWorkIds.toString())
            .append('groupByDepartments', request.groupByDepartments.toString())
            .append('from', Utils.getDateToSend(request.from))
            .append('to', Utils.getDateToSend(request.to));

        return this.http.get<VacationReportPipeModel[]>(this.GENERATE_REPORT_TOTAL_COUNT_URL, {params: params});
    }

    public generateUrlForVacationReportExport(request: GenerateReportExportRequestModel): Observable<any> {
        const params: HttpParams = new HttpParams()
            .append('departmentIds', request.departmentIds.toString())
            .append('employeeIds', request.employeeIds.toString())
            .append('postIds', request.postIds.toString())
            .append('typeWorkIds', request.typeWorkIds.toString())
            .append('from', Utils.getDateToSend(request.from))
            .append('to', Utils.getDateToSend(request.to));

        return this.http.get(this.GENERATE_REPORT_EXPORT_URL, {
            responseType: 'blob',
            params: params
        });
    }

    public getPeriods(): Observable<Page<VacationPeriodModel>> {
        return this.http.get<Page<VacationPeriodModel>>(this.FIND_PERIODS_URL);
    }

    public addVacation(request: VacationModel): Observable<VacationModel> {
        request.vacationFrom = Utils.getDateToSend(request.vacationFrom);
        request.vacationTo = Utils.getDateToSend(request.vacationTo);
        return this.http.post<VacationModel>(this.FIND_VACATION_URL, request);
    }

    public updateVacation(vacationId: number, request: VacationModel): Observable<VacationModel> {
        const url = `${this.VACATION_URL}/${vacationId}`;

        request.vacationFrom = Utils.getDateToSend(request.vacationFrom);
        request.vacationTo = Utils.getDateToSend(request.vacationTo);

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

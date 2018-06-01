import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import {TicketRegisterModel} from './main/ticket-register.model';
import {Page} from '../../../@core/models/page.model';
import {environment} from '../../../../environments/environment';
import {ReportTicketsConfigModel} from './report-tickets-config.model';
import {CreateTicketsConfigModel} from './create-tickets-config.model';
import {TicketModel} from './ticket.model';
import {TicketsFilterModel} from './tickets-filter.model';
import {TypeOperationEnum} from './main/type-operation.enum';
import {Utils} from '../../../@core/utils/utils';
import {TicketsSortModel} from './tickets-sort.model';

@Injectable()
export class TicketsService {
    private readonly getAllTicketRegister: string = `${environment.publicServerUrl}/tickets`;
    private readonly reportTicketRegister: string = `report`;

    constructor(private http: HttpClient) {

    }

    getAllTicketRegisters(page: number = 1, pageSize: number = 15): Observable<Page<TicketRegisterModel>> {
        let params = new HttpParams();

        params = params.append('page', page.toString());
        params = params.append('pagesize', pageSize.toString());

        return this.http.get<Page<TicketRegisterModel>>(this.getAllTicketRegister, {params: params});
    }

    getAllTicketsByRegisterId(regId: number, sort?: TicketsSortModel, filter?: TicketsFilterModel,
                              page: number = 1, pageSize: number = 15): Observable<Page<TicketModel>> {
        let params = new HttpParams();

        params = params.append('page', page.toString());
        params = params.append('pagesize', pageSize.toString());

        if (sort) {
            params = sort.sortId ? params.append('sortId', sort.sortId) : params;
            params = sort.sortCount ? params.append('sortCount', sort.sortCount) : params;
            params = sort.sortTypeOperation ? params.append('sortTypeOperation', sort.sortTypeOperation) : params;
            params = sort.sortEmployeeInitials ? params.append('sortEmployeeInitials', sort.sortEmployeeInitials) : params;
            params = sort.sortEmployeeDepartment ? params.append('sortEmployeeDepartment', sort.sortEmployeeDepartment) : params;
            params = sort.sortEmployeePost ? params.append('sortEmployeePost', sort.sortEmployeePost) : params;
        }

        if (filter) {
            params = filter.filterId ? params.append('filterId', filter.filterId.toString()) : params;
            params = filter.filterCount ? params.append('filterCount', filter.filterCount.toString()) : params;
            params = filter.filterTypeOperation ?
                params.append('filterTypeOperation', Utils.keyFromValue(TypeOperationEnum, filter.filterTypeOperation)) : params;
            params = filter.filterEmployeeInitials ? params.append('filterEmployeeInitials', filter.filterEmployeeInitials) : params;
            params = filter.filterEmployeePost ? params.append('filterEmployeePost', filter.filterEmployeePost) : params;
            params = filter.filterEmployeeDepartment ? params.append('filterEmployeeDepartment', filter.filterEmployeeDepartment) : params;
        }

        const url: string = `${this.getAllTicketRegister}/${regId}/tickets`;

        return this.http.get<Page<TicketModel>>(url, {params: params});
    }

    generateReportAndDownloadUrl(id: number, config: ReportTicketsConfigModel): Observable<TicketRegisterModel>  {
        const url: string = `${this.getAllTicketRegister}/${id}/${this.reportTicketRegister}`;

        return this.http.post<TicketRegisterModel>(url, config);
    }

    generateAndSendReport(id: number, config: ReportTicketsConfigModel): Observable<TicketRegisterModel> {
        const url: string = `${this.getAllTicketRegister}/${id}/${this.reportTicketRegister}/send`;

        return this.http.post<TicketRegisterModel>(url, config);
    }

    createTicketRegisterEmpty(): Observable<TicketRegisterModel> {
        const url: string = `${this.getAllTicketRegister}/empty`;

        return this.http.post<TicketRegisterModel>(url, {});
    }

    createTicketRegisterForAll(config: CreateTicketsConfigModel): Observable<TicketRegisterModel> {
        const url: string = `${this.getAllTicketRegister}/full`;

        return this.http.post<TicketRegisterModel>(url, config);
    }

    deleteTicketRegister(id: number): Observable<any> {
        const url: string = `${this.getAllTicketRegister}/${id}`;

        return this.http.delete(url);
    }

    getTicketRegister(id: number): Observable<TicketRegisterModel> {
        const url: string = `${this.getAllTicketRegister}/${id}`;

        return this.http.get<TicketRegisterModel>(url);
    }
}

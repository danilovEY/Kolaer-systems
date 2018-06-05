import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import {TicketRegisterModel} from './main/ticket-register.model';
import {Page} from '../../../@core/models/page.model';
import {environment} from '../../../../environments/environment';
import {ReportTicketsConfigModel} from './model/report-tickets-config.model';
import {CreateTicketsConfigModel} from './model/create-tickets-config.model';
import {TicketModel} from './model/ticket.model';
import {TicketsFilterModel} from './model/tickets-filter.model';
import {TicketsSortModel} from './model/tickets-sort.model';
import {BaseService} from '../../../@core/services/base.service';
import {TicketRequestModel} from './model/ticket-request.model';
import {RegisterTicketsSortModel} from './model/register-tickets-sort.model';
import {RegisterTicketsFilterModel} from './model/register-tickets-filter.model';

@Injectable()
export class TicketsService extends BaseService {
    private readonly getAllTicketRegister: string = `${environment.publicServerUrl}/tickets`;
    private readonly reportTicketRegister: string = `report`;

    constructor(private http: HttpClient) {
        super();
    }

    getAllTicketRegisters(sort?: RegisterTicketsSortModel, filter?: RegisterTicketsFilterModel,
                          page: number = 1, pageSize: number = 15): Observable<Page<TicketRegisterModel>> {
        let params = new HttpParams();

        params = params.append('page', page.toString());
        params = params.append('pagesize', pageSize.toString());
        params = this.getSortAndFilterParam(params, sort, filter);

        return this.http.get<Page<TicketRegisterModel>>(this.getAllTicketRegister, {params: params});
    }

    getAllTicketsByRegisterId(regId: number, sort?: TicketsSortModel, filter?: TicketsFilterModel,
                              page: number = 1, pageSize: number = 15): Observable<Page<TicketModel>> {
        let params = new HttpParams();

        params = params.append('page', page.toString());
        params = params.append('pagesize', pageSize.toString());
        params = this.getSortAndFilterParam(params, sort, filter);

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

    deleteTicket(regId: number, id: number): Observable<any> {
        const url: string = `${this.getAllTicketRegister}/${regId}/tickets/${id}`;

        return this.http.delete(url);
    }

    getTicketRegister(id: number): Observable<TicketRegisterModel> {
        const url: string = `${this.getAllTicketRegister}/${id}`;

        return this.http.get<TicketRegisterModel>(url);
    }

    updateTicket(regId: number, ticket: TicketModel): Observable<TicketModel> {
        const url: string = `${this.getAllTicketRegister}/${regId}/tickets/${ticket.id}`;

        const ticketRequest: TicketRequestModel = new TicketRequestModel();
        ticketRequest.count = ticket.count;
        ticketRequest.type = ticket.type;
        ticketRequest.employeeId = ticket.employee.id;

        return this.http.put<TicketModel>(url, ticketRequest);
    }

    createTicket(regId: number, ticket: TicketModel): Observable<TicketModel> {
        const url: string = `${this.getAllTicketRegister}/${regId}/tickets`;

        const ticketRequest: TicketRequestModel = new TicketRequestModel();
        ticketRequest.count = ticket.count;
        ticketRequest.type = ticket.type;
        ticketRequest.employeeId = ticket.employee.id;

        return this.http.post<TicketModel>(url, ticketRequest);
    }
}

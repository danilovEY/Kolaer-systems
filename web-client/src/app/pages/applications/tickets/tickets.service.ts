import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import {TicketRegisterModel} from './main/ticket-register.model';
import {Page} from '../../../@core/models/page.model';
import {environment} from '../../../../environments/environment';
import {ReportTicketsConfigModel} from './main/report-tickets-config.model';
import {CreateTicketsConfigModel} from './main/create-tickets-config.model';
import {TicketModel} from './ticket.model';

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

    getAllTicketsByRegisterId(regId: number, page: number = 1, pageSize: number = 15): Observable<Page<TicketModel>> {
        let params = new HttpParams();

        params = params.append('page', page.toString());
        params = params.append('pagesize', pageSize.toString());

        const url: string = `${this.getAllTicketRegister}/${regId}/tickets`;

        return this.http.get<Page<TicketModel>>(this.getAllTicketRegister, {params: params});
    }

    generateReportAndDownloadUrl(id: number, config: ReportTicketsConfigModel): Observable<TicketRegisterModel>  {
        const url: string = `${this.getAllTicketRegister}/${id}/${this.reportTicketRegister}`;

        return this.http.post<TicketRegisterModel>(url, config);
    }

    generateAndSendReport(id: number, config: ReportTicketsConfigModel): Observable<TicketRegisterModel> {
        const url: string = `${this.getAllTicketRegister}/${id}/${this.reportTicketRegister}/send`;

        return this.http.post<TicketRegisterModel>(url, config);
    }

    createTicketRegister(config: CreateTicketsConfigModel): Observable<TicketRegisterModel> {
        const url: string = `${this.getAllTicketRegister}/full`;

        return this.http.post<TicketRegisterModel>(url, config);
    }

    deleteTicketRegister(id: number): Observable<any> {
        const url: string = `${this.getAllTicketRegister}/${id}`;

        return this.http.delete(url);
    }
}

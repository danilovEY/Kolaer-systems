import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs/index';
import {Page} from '../../../@core/models/page.model';
import {environment} from '../../../../environments/environment';
import {BaseService} from '../../../@core/services/base.service';
import {QueueTargetModel} from '../../../@core/models/queue-target.model';
import {QueueRequestModel} from '../../../@core/models/queue-request.model';

@Injectable()
export class QueueService extends BaseService {
    private readonly getAllQueueTargetsUrl: string = `${environment.publicServerUrl}/queue`;

    constructor(private http: HttpClient) {
        super();
    }

    getAllQueueTargets(page: number = 1, pageSize: number = 15): Observable<Page<QueueTargetModel>> {
        let params = new HttpParams();

        params = params.append('page', page.toString());
        params = params.append('pagesize', pageSize.toString());

        return this.http.get<Page<QueueTargetModel>>(this.getAllQueueTargetsUrl, {params: params});
    }

    getAllQueueRequest(targetId: number, page: number = 1, pageSize: number = 15): Observable<Page<QueueRequestModel>> {
        let params = new HttpParams();

        params = params.append('page', page.toString());
        params = params.append('pagesize', pageSize.toString());

        const url: string = `${this.getAllQueueTargetsUrl}/${targetId}/request`;

        return this.http.get<Page<QueueRequestModel>>(url, {params: params});
    }
}

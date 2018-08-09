import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs/index';
import {Page} from '../../../@core/models/page.model';
import {environment} from '../../../../environments/environment';
import {BaseService} from '../../../@core/services/base.service';
import {QueueTargetModel} from '../../../@core/models/queue-target.model';
import {QueueRequestModel} from '../../../@core/models/queue-request.model';
import {QueueScheduleModel} from '../../../@core/models/queue-schedule.model';
import {QueueFilterPageModel} from '../../../@core/models/page-queue-target-filter.model';
import {QueueSortModel} from '../../../@core/models/queue-sort.model';

@Injectable()
export class QueueService extends BaseService {
    private readonly queueTargetsUrl: string = `${environment.publicServerUrl}/queue`;
    private readonly queueSchedulersUrl: string = `${this.queueTargetsUrl}/scheduler`;

    constructor(private http: HttpClient) {
        super();
    }

    getAllQueueTargets(page: number = 1, pageSize: number = 15): Observable<Page<QueueTargetModel>> {
        let params = new HttpParams();

        params = params.append('page', page.toString());
        params = params.append('pagesize', pageSize.toString());

        return this.http.get<Page<QueueTargetModel>>(this.queueTargetsUrl, {params: params});
    }

    getSchedulers(sort: QueueSortModel, filter: QueueFilterPageModel): Observable<Page<QueueScheduleModel>> {
        const params: HttpParams = this.getSortAndFilterParam(new HttpParams(), sort, filter);

        return this.http.get<Page<QueueScheduleModel>>(this.queueSchedulersUrl, {params: params});
    }

    getSchedulers(page: number = 1, pageSize: number = 15): Observable<Page<QueueScheduleModel>> {
        let params = new HttpParams();

        params = params.append('page', page.toString());
        params = params.append('pagesize', pageSize.toString());

        return this.http.get<Page<QueueScheduleModel>>(this.queueSchedulersUrl, {params: params});
    }

    addQueueTargets(queueTarget: QueueTargetModel): Observable<QueueTargetModel> {
        return this.http.post<QueueTargetModel>(this.queueTargetsUrl, queueTarget);
    }

    updateQueueTargets(targetId: number, queueTarget: QueueTargetModel): Observable<QueueTargetModel> {
        const url: string = `${this.queueTargetsUrl}/${targetId}`;

        return this.http.put<QueueTargetModel>(url, queueTarget);
    }

    deleteQueueTargets(targetId: number): Observable<Object> {
        const url: string = `${this.queueTargetsUrl}/${targetId}`;

        return this.http.delete<Object>(url);
    }

    getAllQueueRequest(targetId: number, page: number = 1, pageSize: number = 15): Observable<Page<QueueRequestModel>> {
        let params = new HttpParams();

        params = params.append('page', page.toString());
        params = params.append('pagesize', pageSize.toString());

        const url: string = `${this.queueTargetsUrl}/${targetId}/request`;

        return this.http.get<Page<QueueRequestModel>>(url, {params: params});
    }

    addQueueRequest(targetId: number, queueRequest: QueueRequestModel): Observable<QueueRequestModel> {
        const url: string = `${this.queueTargetsUrl}/${targetId}/request`;

        return this.http.post<QueueRequestModel>(url, queueRequest);
    }

    updateQueueRequest(targetId: number, requestId: number, queueRequest: QueueRequestModel): Observable<QueueRequestModel> {
        const url: string = `${this.queueTargetsUrl}/${targetId}/request/${requestId}`;

        return this.http.put<QueueRequestModel>(url, queueRequest);
    }

    deleteQueueRequest(targetId: number, requestId: number): Observable<Object> {
        const url: string = `${this.queueTargetsUrl}/${targetId}/request/${requestId}`;

        return this.http.delete<Object>(url);
    }
}

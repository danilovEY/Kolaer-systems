import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {environment} from '../../../environments/environment';
import {Observable} from 'rxjs/index';
import 'rxjs/add/observable/of';
import {Page} from '../models/page.model';
import {BaseService} from './base.service';
import {PlacementModel} from "../models/placement.model";
import {PlacementSortModel} from "../models/placement-sort.model";
import {PlacementFilterModel} from "../models/placement-filter.model";

@Injectable()
export class PlacementService extends BaseService {
    private readonly getPlacementUrl: string = environment.publicServerUrl + '/placement';

    constructor(private _httpClient: HttpClient) {
        super();
    }

    getAllPlacements(sort: PlacementSortModel, filter: PlacementFilterModel,
                     page: number = 1, pageSize: number = 15): Observable<Page<PlacementModel>> {
        let params = new HttpParams();

        params = params.append('page', page.toString()).append('pagesize', pageSize.toString());
        params = this.getSortAndFilterParam(params, sort, filter);

        return this._httpClient.get<Page<PlacementModel>>(this.getPlacementUrl, {params: params});
    }
}

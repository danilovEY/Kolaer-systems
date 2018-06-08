import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {environment} from '../../../environments/environment';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/observable/of';
import {Page} from '../models/page.model';
import {BaseService} from './base.service';
import {PostSortModel} from '../models/post-sort.model';
import {PostFilterModel} from '../models/post-filter.model';
import {PostModel} from '../models/post.model';

@Injectable()
export class PostService extends BaseService {
    private readonly getPostUrl: string = environment.publicServerUrl + '/posts';

    constructor(private _httpClient: HttpClient) {
        super();
    }

    getAllPosts(sort?: PostSortModel, filter?: PostFilterModel,
                    page: number = 1, pageSize: number = 15): Observable<Page<PostModel>> {
        let params = new HttpParams();

        params = params.append('page', page.toString()).append('pagesize', pageSize.toString());
        params = this.getSortAndFilterParam(params, sort, filter);

        return this._httpClient.get<Page<PostModel>>(this.getPostUrl, {params: params});
    }
}

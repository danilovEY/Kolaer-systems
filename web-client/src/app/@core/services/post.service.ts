import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {environment} from '../../../environments/environment';
import {Observable} from 'rxjs/index';
import 'rxjs/add/observable/of';
import {Page} from '../models/page.model';
import {BaseService} from './base.service';
import {PostSortModel} from '../models/post-sort.model';
import {PostFilterModel} from '../models/post-filter.model';
import {PostModel} from '../models/post.model';
import {PostRequestModel} from '../models/post-request.model';
import {FindPostRequestModel} from '../models/post/find-post-request.model';

@Injectable()
export class PostService extends BaseService {
    private readonly getPostUrl: string = environment.publicServerUrl + '/posts';
    private readonly FIND_POST_URL: string = this.getPostUrl + '/find';

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

    find(request: FindPostRequestModel): Observable<Page<PostModel>> {
        let params = new HttpParams();

        params = params.append('number', String(request.number))
            .append('query', request.query)
            .append('pagesize', String(request.pageSize))
            .append('onOnePage', String(request.onOnePage))
            .append('departmentIds', request.departmentIds.toString());

        return this._httpClient.get<Page<PostModel>>(this.FIND_POST_URL, {params: params})
    }

    createPost(post: PostRequestModel): Observable<PostModel> {
        return this._httpClient.post<PostModel>(this.getPostUrl, post);
    }

    updateDepartment(postId: number, department: PostRequestModel): Observable<PostModel> {
        const url = `${this.getPostUrl}/${postId}`;
        return this._httpClient.put<PostModel>(url, department);
    }

    deleteDepartment(postId: number): Observable<any> {
        const url = `${this.getPostUrl}/${postId}`;
        return this._httpClient.delete(url);
    }
}

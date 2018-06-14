import {CustomDataSource} from '../../../../@core/models/custom.data-source';
import {Page} from '../../../../@core/models/page.model';
import {PostModel} from '../../../../@core/models/post.model';
import {PostService} from '../../../../@core/services/post.service';
import {PostSortModel} from '../../../../@core/models/post-sort.model';
import {PostFilterModel} from '../../../../@core/models/post-filter.model';

export class PostsDataSource extends CustomDataSource<PostModel> {

    constructor(private postService: PostService) {
        super();
    }

    loadElements(page: number, pageSize: number): Promise<PostModel[]> {
        const postSortModel: PostSortModel =
            this.getFilterModel(new PostSortModel());

        const postFilterModel: PostFilterModel =
            this.getSortModel(new PostFilterModel());

        return this.postService.getAllPosts(postSortModel, postFilterModel,
            page, pageSize)
            .toPromise()
            .then((response: Page<PostModel>) => this.setDataPage(response));
    }
}

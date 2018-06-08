import {Component, OnInit, ViewChild} from '@angular/core';
import {Column} from 'ng2-smart-table/lib/data-set/column';
import {CustomTableComponent} from '../../../../@theme/components';
import {TableEventAddModel} from '../../../../@theme/components/table/table-event-add.model';
import {TableEventDeleteModel} from '../../../../@theme/components/table/table-event-delete.model';
import {TableEventEditModel} from '../../../../@theme/components/table/table-event-edit.model';
import {PostService} from '../../../../@core/services/post.service';
import {PostsDataSource} from './posts.data-source';
import {PostModel} from '../../../../@core/models/post.model';
import {Cell} from "ng2-smart-table";
import {TypePost} from "../../../../@core/models/type-post.emun";
import {Utils} from "../../../../@core/utils/utils";

@Component({
    selector: 'posts',
    styleUrls: ['./posts.component.scss'],
    templateUrl: './posts.component.html'
})
export class PostsComponent implements OnInit {
    @ViewChild('postsTable')
    postsTable: CustomTableComponent;

    postsColumns: Column[] = [];
    postsSource: PostsDataSource;
    postsLoading: boolean = true;

    constructor(private postService: PostService) {
        this.postsSource = new PostsDataSource(this.postService);
        this.postsSource.onLoading().subscribe(load => this.postsLoading = load);
    }

    ngOnInit() {
        const abbreviatedNameColumn: Column = new Column('abbreviatedName', {
            title: 'Сокращение',
            type: 'string'
        }, null);

        const nameColumn: Column = new Column('name', {
            title: 'Имя',
            type: 'string'
        }, null);

        const typeColumn: Column = new Column('type', {
            title: 'Тип',
            type: 'string',
            filter: {
                type: 'list',
                config: {
                    selectText: 'Тип...',
                    list: [
                        { value: null, title: 'Нет' },
                        { value: Utils.keyFromValue(TypePost, TypePost.CATEGORY), title: TypePost.CATEGORY },
                        { value: Utils.keyFromValue(TypePost, TypePost.DISCHARGE), title: TypePost.DISCHARGE },
                        { value: Utils.keyFromValue(TypePost, TypePost.GROUP), title: TypePost.GROUP },
                    ],
                },
            },
            editor: {
                type: 'list',
                config: {
                    list: [
                        { value: null, title: 'Нет' },
                        { value: Utils.keyFromValue(TypePost, TypePost.CATEGORY), title: TypePost.CATEGORY },
                        { value: Utils.keyFromValue(TypePost, TypePost.DISCHARGE), title: TypePost.DISCHARGE },
                        { value: Utils.keyFromValue(TypePost, TypePost.GROUP), title: TypePost.GROUP },
                    ],
                },
            },
            valuePrepareFunction(a: any, value: PostModel, cell: Cell) {
                return TypePost[value.type];
            }
        }, null);

        const rangColumn: Column = new Column('rang', {
            title: 'Ранг',
            type: 'number'
        }, null);


        this.postsColumns.push(abbreviatedNameColumn,
            nameColumn,
            rangColumn,
            typeColumn);
    }

    postsEdit(event: TableEventEditModel<PostModel>) {

    }

    postsCreate(event: TableEventAddModel<PostModel>) {

    }

    postsDelete(event: TableEventDeleteModel<PostModel>) {

    }
}

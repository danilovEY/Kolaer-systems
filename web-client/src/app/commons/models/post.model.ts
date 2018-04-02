import {TypePost} from './type-post.emun';
import {BaseModel} from './base.model';

export class PostModel extends BaseModel {
    abbreviatedName: string;
    code: string;
    name: string;
    rang: number;
    type: TypePost;
}

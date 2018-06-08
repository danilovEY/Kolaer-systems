import {SortTypeEnum} from './sort-type.enum';

export class PostSortModel {
    constructor(public sortId?: SortTypeEnum,
                public sortName?: SortTypeEnum) {
    }
}

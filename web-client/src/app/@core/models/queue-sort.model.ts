import {SortTypeEnum} from './sort-type.enum';

export class QueueSortModel {
    constructor(public sortId?: SortTypeEnum,
                public sortTitle?: SortTypeEnum) {
    }
}

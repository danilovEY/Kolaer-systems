import {SortTypeEnum} from './sort-type.enum';

export class SortModel {
    constructor(public sortField?: string,
                public sortType?: SortTypeEnum) {
    }
}

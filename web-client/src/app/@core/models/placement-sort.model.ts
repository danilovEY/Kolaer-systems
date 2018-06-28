import {SortTypeEnum} from './sort-type.enum';

export class PlacementSortModel {
    constructor(public sortId?: SortTypeEnum,
                public sortName?: SortTypeEnum) {
    }
}

import {SortTypeEnum} from '../../../../@core/models/sort-type.enum';

export class RegisterTicketsSortModel {
    constructor(public sortId: SortTypeEnum = SortTypeEnum.DESC) {
    }
}

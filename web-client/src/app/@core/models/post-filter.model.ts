export class PostFilterModel {
    constructor(public filterId?: number,
                public filterName?: string,
                public filterAbbreviatedName?: string,
                public filterDeleted: boolean = false) {
    }
}

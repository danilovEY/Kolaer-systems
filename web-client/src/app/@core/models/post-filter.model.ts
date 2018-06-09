export class PostFilterModel {
    constructor(public filterId?: number,
                public filterName?: string,
                public filterDeleted: boolean = false) {
    }
}

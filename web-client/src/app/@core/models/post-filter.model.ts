export class PostFilterModel {
    constructor(public filterId?: number,
                public filterDeleted: boolean = false,
                public filterName?: string) {
    }
}

export class DepartmentFilterModel {
    constructor(public filterId?: number,
                public filterName?: string,
                public filterAbbreviatedName?: string,
                public filterDeleted: boolean = false) {
    }
}

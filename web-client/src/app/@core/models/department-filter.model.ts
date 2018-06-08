export class DepartmentFilterModel {
    constructor(public filterId?: number,
                public filterDeleted: boolean = false,
                public filterName?: string) {
    }
}

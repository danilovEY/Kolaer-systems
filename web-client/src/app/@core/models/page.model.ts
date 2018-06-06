export class Page<T> {
    constructor(public data: T[] = [],
                public number: number = 1,
                public pageSize: number = 15,
                public total: number = data.length) {
    }
}

export class Page<T> {
    constructor(public data: T[] = [],
                public pageNum: number = 1,
                public pageSize: number = data.length,
                public total: number = data.length) {
    }
}

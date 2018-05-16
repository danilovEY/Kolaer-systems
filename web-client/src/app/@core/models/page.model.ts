import {JsonProperty} from 'json-typescript-mapper';

export class Page<T> {
    data: T[];
    @JsonProperty('number')
    page: number;
    pageSize: number;
    total: number;
}

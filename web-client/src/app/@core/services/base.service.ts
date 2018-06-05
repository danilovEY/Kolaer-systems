import {HttpParams} from '@angular/common/http';

export class BaseService {

    getSortAndFilterParam(httpParams: HttpParams, sortParam: any, filterParam: any): HttpParams {
        if (sortParam) {
            for (const key of Object.keys(sortParam)) {
                httpParams = sortParam[key] !== undefined ? httpParams.append(key, sortParam[key]) : httpParams;
            }
        }

        if (filterParam) {
            for (const key of Object.keys(filterParam)) {
                httpParams = filterParam[key] !== undefined ? httpParams.append(key, filterParam[key]) : httpParams;
            }
        }

        return httpParams;
    }
}

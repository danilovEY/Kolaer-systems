import {HttpParams} from '@angular/common/http';

export class BaseService {

    getSortAndFilterParam(httpParams: HttpParams, sortParam: any, filterParam: any): HttpParams {
        if (sortParam) {
            for (const key of Object.keys(sortParam)) {
                httpParams = sortParam[key] !== undefined && sortParam[key] !== null
                    ? httpParams.append(key, sortParam[key]) : httpParams;
            }
        }

        if (filterParam) {
            for (const key of Object.keys(filterParam)) {
                httpParams = filterParam[key] !== undefined && filterParam[key] !== null
                    ? httpParams.append(key, filterParam[key]) : httpParams;
            }
        }

        return httpParams;
    }

    includeHttpParams(httpParams: HttpParams, paramModel: any): HttpParams {
        if (paramModel) {
            for (const key of Object.keys(paramModel)) {
                httpParams = paramModel[key] !== undefined &&
                paramModel[key] !== null && (paramModel[key].length === undefined || paramModel[key].length > 0)
                    ? httpParams.append(key, String(paramModel[key]))
                    : httpParams;
            }
        }

        return httpParams;
    }

    convertModel(model: any): any {
        return model;
    }
}

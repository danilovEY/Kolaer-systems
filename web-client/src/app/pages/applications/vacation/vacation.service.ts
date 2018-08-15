import {Injectable} from '@angular/core';
import {BaseService} from '../../../@core/services/base.service';
import {HttpClient} from '@angular/common/http';

@Injectable()
export class VacationService extends BaseService {

    constructor(private http: HttpClient) {
        super();
    }

}

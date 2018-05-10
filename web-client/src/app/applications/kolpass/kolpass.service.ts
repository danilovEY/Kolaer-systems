import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import 'rxjs/add/operator/map';
import {AuthenticationRestService} from '../../commons/modules/auth/authentication-rest.service';

@Injectable()
export class KolpassService {
    constructor(private _authService: AuthenticationRestService,
                private _httpClient: HttpClient) {

    }
}

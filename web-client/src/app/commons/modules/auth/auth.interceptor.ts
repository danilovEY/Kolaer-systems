import {HttpClient} from '@angular/common/http';
import {AuthenticationRestService} from './authentication-rest.service';

export interface AuthInterceptor {
    init(http: HttpClient, authService: AuthenticationRestService);
}
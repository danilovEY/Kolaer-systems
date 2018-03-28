import {AuthenticationService} from './authentication.service';
import {HttpClient} from '@angular/common/http';

export interface AuthInterceptor {
    init(http: HttpClient, authService: AuthenticationService);
}

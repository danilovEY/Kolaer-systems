import {HttpClient, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/index';
import {AuthInterceptor} from './auth.interceptor';
import {AuthenticationRestService} from './authentication-rest.service';

@Injectable()
export class TokenApplyInterceptor implements HttpInterceptor, AuthInterceptor {
    private _authService: AuthenticationRestService;
    
    init(http: HttpClient, authService: AuthenticationRestService) {
        this._authService = authService;
    }
    
    constructor() {
        
    }
    
    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        if (!this._authService.getToken()) {
            return next.handle(req);
        }

        const authReq = req.clone({
            headers: req.headers.append('x-token', this._authService.getToken().token)
        });

        return next.handle(authReq);
    }
    
}

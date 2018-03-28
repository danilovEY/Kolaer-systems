import {HttpClient, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {AuthInterceptor} from './auth.interceptor';
import {AuthenticationService} from './authentication.service';

@Injectable()
export class TokenApplyInterceptor implements HttpInterceptor, AuthInterceptor {
    private _authService: AuthenticationService;
    
    init(http: HttpClient, authService: AuthenticationService) {
        this._authService = authService;
    }
    
    constructor() {
        
    }
    
    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        if (!req.url.includes('rest/') || !this._authService.getToken()) {
            return next.handle(req);
        }

        const authReq = req.clone({
            headers: req.headers.set('x-token', this._authService.getToken().token)
        });

        return next.handle(authReq);
    }
    
}

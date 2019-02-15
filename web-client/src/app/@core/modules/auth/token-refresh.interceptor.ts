import {HttpClient, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable, Subscriber} from 'rxjs/index';
import {AuthInterceptor} from './auth.interceptor';
import {Router} from '@angular/router';
import 'rxjs/add/operator/catch';
import {AuthenticationRestService} from './authentication-rest.service';

@Injectable()
export class TokenRefreshInterceptor implements HttpInterceptor, AuthInterceptor {
    private _authService: AuthenticationRestService;
    private _isRefreshToken: boolean = false;
    private _http: HttpClient;
    private _requestMap: Map<Subscriber<any>, HttpRequest<any>> = new Map<Subscriber<any>, HttpRequest<any>>();
    private _router: Router;

    init(http: HttpClient, authService: AuthenticationRestService) {
        this._authService = authService;
        this._http = http;
    }
    
    constructor() {
        
    }
    
    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        if (!this._authService.getToken()) {
            return next.handle(req);
        }

        return new Observable<HttpEvent<any>>((subscriber => {
            const originRequest = next.handle(req)
                .subscribe(
                    response => subscriber.next(response),
                    error => {
                        if (error.status === 401) {
                            this.processErrorRequest(subscriber, req);
                        }

                        subscriber.error(error);
                    },
                    () => subscriber.complete()
                );

            return () => originRequest.unsubscribe();
        }));
    }

    processErrorRequest(subscribe: Subscriber<any>, request: HttpRequest<any>) {
        this._requestMap.set(subscribe, request);
        if (!this._isRefreshToken) {
            this._isRefreshToken = true;
            
            this._authService.refreshToken()
                .subscribe(
                    next => this.repeatFailedRequest(),
                    error => {
                        this._authService.logout().subscribe(result => {});
                    },
                    () => {
                        this._isRefreshToken = false;
                        this._requestMap.clear();
                    }
                );
        }
    }

    repeatFailedRequest(): void {
        if (this._authService.getToken()) {
            this._requestMap.forEach((value: HttpRequest<any>, key: Subscriber<any>) => {
                const requestWithToken = value.clone({
                    headers: value.headers.append('x-token', this._authService.getToken().token)
                });

                this.repeatRequest(requestWithToken, key);
            });
        }
    }

    repeatRequest(requestWithToken: HttpRequest<any>, subscribe: Subscriber<any>): void {
        this._http.request(requestWithToken)
            .subscribe(
                response => subscribe.next(response),
                error => {
                    if (error.status === 401) {
                        this._authService.logout().subscribe(result => {});
                    }

                    subscribe.error(error);
                },
                () => subscribe.complete()
            );
    }
    
}

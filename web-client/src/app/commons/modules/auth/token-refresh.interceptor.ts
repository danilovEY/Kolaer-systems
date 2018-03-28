import {HttpClient, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {AuthInterceptor} from './auth.interceptor';
import {AuthenticationService} from './authentication.service';
import {Subscriber} from 'rxjs/Subscriber';

@Injectable()
export class TokenRefreshInterceptor implements HttpInterceptor, AuthInterceptor {
    private _authService: AuthenticationService;
    private _isRefreshToken: boolean = false;
    private _http: HttpClient;
    private _requestMap: Map<Subscriber<any>, HttpRequest<any>> = new Map<Subscriber<any>, HttpRequest<any>>();

    init(http: HttpClient, authService: AuthenticationService) {
        this._authService = authService;
        this._http = http;
    }
    
    constructor() {
        
    }
    
    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        if (!req.url.includes('rest/') || !this._authService.getToken()) {
            return next.handle(req);
        }

        return new Observable<HttpEvent<any>>((subscriber => {
            const originRequest = next.handle(req)
                .subscribe(
                    response => subscriber.next(response),
                    error => {
                        if (error.status === 401) {
                            this.processErrorRequest(subscriber, req);
                        } else {
                            subscriber.error(error);
                        }
                    },
                    () => subscriber.complete()
                );

            return () => originRequest.unsubscribe();
        }));
    }

    processErrorRequest(subscribe: Subscriber<any>, request: HttpRequest<any>) {
        if (this._isRefreshToken) {
            this._requestMap.set(subscribe, request);
        } else {
            this._isRefreshToken = true;

            this._authService.refreshToken()
                .subscribe(
                    token => this.repeatFailedRequest(),
                    error => this._authService.logout(),
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
                    headers: value.headers.set('token', this._authService.getToken().token)
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
                        this._authService.logout();
                    }

                    subscribe.error(error);
                },
                () => subscribe.complete()
            );
    }
    
}

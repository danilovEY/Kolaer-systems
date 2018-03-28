import {APP_INITIALIZER, Inject, Injector, NgModule} from '@angular/core';
import {AuthenticationRestService} from './authentication-rest.service';
import {HTTP_INTERCEPTORS, HttpClient, HttpClientModule} from '@angular/common/http';
import {AuthenticationService} from './authentication.service';
import {TokenApplyInterceptor} from './token-apply.interceptor';
import {AuthInterceptor} from './auth.interceptor';
import {TokenRefreshInterceptor} from './token-refresh.interceptor';


@NgModule({
    exports: [
        HttpClientModule
    ],
    providers: [
        {
            provide: 'AuthenticationService',
            useClass: AuthenticationRestService
        },
        {
            provide: APP_INITIALIZER,
            useFactory: refreshToken,
            deps: ['AuthenticationService'],
            multi: true
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: TokenApplyInterceptor,
            multi: true
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: TokenRefreshInterceptor,
            multi: true
        }
    ]
})
export class AuthModule {

    constructor(@Inject('AuthenticationService') private _authService: AuthenticationService,
                private _injector: Injector,
                private _http: HttpClient) {
        const interceptors: AuthInterceptor[] = _injector.get<AuthInterceptor[]>(HTTP_INTERCEPTORS)
            .filter(interceptor => interceptor.init);
        interceptors.forEach(interceptor => interceptor.init(this._http, this._authService));

        this._authService.ngOnInit();

    }
}

export function refreshToken(auth: AuthenticationService) {
    return () => auth.refreshToken();
}



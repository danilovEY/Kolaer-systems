import {APP_INITIALIZER, Injector, NgModule} from '@angular/core';
import {AuthenticationRestService} from './authentication-rest.service';
import {HTTP_INTERCEPTORS, HttpClient, HttpClientModule} from '@angular/common/http';
import {TokenApplyInterceptor} from './token-apply.interceptor';
import {AuthInterceptor} from './auth.interceptor';
import {TokenRefreshInterceptor} from './token-refresh.interceptor';
import {Router} from '@angular/router';
import {AuthGuardService} from './auth-guard.service';


@NgModule({
    exports: [
        HttpClientModule
    ],
    providers: [
        AuthGuardService,
        AuthenticationRestService,
        {
            provide: APP_INITIALIZER,
            useFactory: refreshToken,
            deps: [AuthenticationRestService],
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

    constructor(private _authService: AuthenticationRestService,
                private _injector: Injector,
                private _http: HttpClient,
                private _router: Router) {
        this._authService.ngOnInit();

        const interceptors: AuthInterceptor[] = _injector.get<AuthInterceptor[]>(HTTP_INTERCEPTORS)
            .filter(interceptor => interceptor.init);

        interceptors.forEach(interceptor => interceptor.init(this._http, this._authService));

    }
}

export function refreshToken(auth: AuthenticationRestService) {
    return () => auth.refreshToken();
}



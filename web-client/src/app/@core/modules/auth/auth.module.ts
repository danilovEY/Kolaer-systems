import {APP_INITIALIZER, Injector, ModuleWithProviders, NgModule, Optional, SkipSelf} from '@angular/core';
import {AuthenticationRestService} from './authentication-rest.service';
import {HTTP_INTERCEPTORS, HttpClient, HttpClientModule} from '@angular/common/http';
import {TokenApplyInterceptor} from './token-apply.interceptor';
import {AuthInterceptor} from './auth.interceptor';
import {TokenRefreshInterceptor} from './token-refresh.interceptor';
import {Router} from '@angular/router';
import {AuthGuardService} from './auth-guard.service';
import {AdminGuardService} from './admin-guard.service';
import {VacationGuardService} from './vacation-guard.service';


@NgModule({
    exports: [
        HttpClientModule
    ],
    providers: [
        AuthGuardService,
        AdminGuardService,
        VacationGuardService,
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

    static forRoot(): ModuleWithProviders {
        return {
            ngModule: AuthModule,
            providers: [
                AuthenticationRestService,
                AuthGuardService,
                AdminGuardService,
                VacationGuardService
            ]
        };
    }

    constructor(@Optional() @SkipSelf() parentModule: AuthModule,
                private _authService: AuthenticationRestService,
                private _injector: Injector,
                private _http: HttpClient,
                private _router: Router) {
        if (parentModule) {
            throw new Error(
                'AuthModule is already loaded. Import it in the AppModule only');
        }

        this._authService.ngOnInit();

        const interceptors: AuthInterceptor[] = _injector.get<AuthInterceptor[]>(HTTP_INTERCEPTORS)
            .filter(interceptor => interceptor.init);

        interceptors.forEach(interceptor => interceptor.init(this._http, this._authService));
    }

}

export function refreshToken(auth: AuthenticationRestService) {
    return () => auth.refreshToken();
}



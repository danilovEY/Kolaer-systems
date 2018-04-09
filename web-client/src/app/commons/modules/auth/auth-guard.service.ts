import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from '@angular/router';
import {Inject, Injectable} from '@angular/core';
import {AuthenticationService} from './authentication.service';
import {Observable} from 'rxjs/Observable';

@Injectable()
export class AuthGuardService implements CanActivate {
    constructor(@Inject('AuthenticationService') private _authService: AuthenticationService,
                private _router: Router) {

    }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
        if (this._authService.isAuthentication()) {
            return true;
        } else {
            this._router.navigate(['/home/dashboard'], {
                queryParams: {
                    login: false
                }
            });
            return false;
        }
    }
}

import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from '@angular/router';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {AuthenticationRestService} from './authentication-rest.service';

@Injectable()
export class AuthGuardService implements CanActivate {
    constructor(private _authService: AuthenticationRestService,
                private _router: Router) {

    }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
        if (this._authService.authentication) {
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

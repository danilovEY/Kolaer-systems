import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from '@angular/router';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/index';
import {AccountService} from '../../services/account.service';
import {SimpleAccountModel} from '../../models/simple-account.model';
import {map, tap} from 'rxjs/internal/operators';

@Injectable()
export class AdminGuardService implements CanActivate {
    constructor(private _accountService: AccountService,
                private _router: Router) {
    }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
       return this._accountService.getCurrentAccount(false)
            .pipe(
                map((account: SimpleAccountModel) => account.accessOit),
                tap((isAdmin: boolean) => {
                    if (!isAdmin) {
                        this._router.navigate(['/auth/login']);
                    }
                })
            );
    }
}

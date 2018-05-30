import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from '@angular/router';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {AccountService} from '../../services/account.service';
import {SimpleAccountModel} from '../../models/simple-account.model';
import {tap} from 'rxjs/operators';

@Injectable()
export class AdminGuardService implements CanActivate {
    constructor(private _accountService: AccountService,
                private _router: Router) {
    }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
       return this._accountService.getCurrentAccount(false)
            .map((account: SimpleAccountModel) => account.accessOit)
            .pipe(
                tap((isAdmin: boolean) => {
                    if (!isAdmin) {
                        this._router.navigate(['/auth/login']);
                    }
                })
            );
    }
}

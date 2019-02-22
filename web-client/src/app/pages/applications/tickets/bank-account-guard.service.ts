import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from '@angular/router';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/index';
import {map, tap} from 'rxjs/internal/operators';
import {AccountService} from "../../../@core/services/account.service";
import {SimpleAccountModel} from "../../../@core/models/simple-account.model";
import {RoleConstant} from "../../../@core/constants/role.constant";

@Injectable()
export class BankAccountGuardService implements CanActivate {
    constructor(private _accountService: AccountService,
                private _router: Router) {
    }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
       return this._accountService.getCurrentAccount(false)
            .pipe(
                map((account: SimpleAccountModel) => account.access.includes(RoleConstant.BANK_ACCOUNTS_READ)),
                tap((hasAccess: boolean) => {
                    if (!hasAccess) {
                        this._router.navigate(['/auth/login']);
                    }
                })
            );
    }
}

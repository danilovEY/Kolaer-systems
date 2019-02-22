import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from '@angular/router';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/index';
import {AccountService} from '../../../@core/services/account.service';
import {SimpleAccountModel} from '../../../@core/models/simple-account.model';
import {map, tap} from 'rxjs/internal/operators';
import {RoleConstant} from "../../../@core/constants/role.constant";

@Injectable()
export class VacationGuardService implements CanActivate {
    constructor(private _accountService: AccountService,
                private _router: Router) {
    }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
       return this._accountService.getCurrentAccount(false)
            .pipe(
                map((account: SimpleAccountModel) =>
                    account.access.includes(RoleConstant.VACATIONS_READ) ||
                    account.access.includes(RoleConstant.VACATIONS_READ_DEPARTMENT)
                ),
                tap((hasAccess: boolean) => {
                    if (!hasAccess) {
                        this._router.navigate(['/auth/login']);
                    }
                })
            );
    }
}

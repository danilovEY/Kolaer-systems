import {Component, OnInit} from '@angular/core';

import {NbMenuService, NbSidebarService} from '@nebular/theme';
import {NbMenuItem} from '@nebular/theme/components/menu/menu.service';
import {AuthenticationRestService} from '../../../@core/modules/auth/authentication-rest.service';
import {AccountService} from '../../../@core/services/account.service';
import {SimpleAccountModel} from "../../../@core/models/simple-account.model";
import {RouterNavigatorService} from "../../../@core/services/router-navigator.service";

@Component({
    selector: 'ngx-header',
    styleUrls: ['./header.component.scss'],
    templateUrl: './header.component.html',
})
export class HeaderComponent implements OnInit {
    private readonly profileMenuItem: NbMenuItem = new NbMenuItem();
    private readonly singOutMenuItem: NbMenuItem = new NbMenuItem();
    private readonly singInMenuItem: NbMenuItem = new NbMenuItem();

    userMenu: NbMenuItem[] = [];
    accountModel: SimpleAccountModel = undefined;

    constructor(private sidebarService: NbSidebarService,
                private menuService: NbMenuService,
                private accountService: AccountService,
                public authService: AuthenticationRestService,
                public routerNavigatorService: RouterNavigatorService) {
    }

    ngOnInit() {
        if (this.authService.authentication && !this.accountModel) {
            this.accountService
                .getCurrentAccount()
                .subscribe((account: SimpleAccountModel) => this.accountModel = account,
                    error2 => {console.log(error2)});
        }

        this.profileMenuItem.title = 'Профиль';
        this.profileMenuItem.link = '/pages/profile';
        this.profileMenuItem.icon = 'fa fa-user';

        this.singOutMenuItem.title = 'Выход';
        this.singOutMenuItem.link = '/auth/logout';
        this.singOutMenuItem.icon = 'ion-log-out';

        this.singInMenuItem.title = 'Вход';
        this.singInMenuItem.link = '/auth/login';
        this.singInMenuItem.icon = 'ion-log-in';

        if (!this.authService.authentication) {
            this.profileMenuItem.hidden = true;
            this.singOutMenuItem.hidden = true;
        } else {
            this.singInMenuItem.hidden = true;
        }

        this.userMenu.push(this.profileMenuItem, this.singOutMenuItem, this.singInMenuItem);
    }

    toggleSidebar(): boolean {
        this.sidebarService.toggle(true, 'menu-sidebar');
        return false;
    }

    goToHome() {
        this.menuService.navigateHome();
    }

    navPrev() {
        this.routerNavigatorService.navigateToPreviousUrl();
    }

    navNext() {
        this.routerNavigatorService.navigateToNextUrl();
    }
}

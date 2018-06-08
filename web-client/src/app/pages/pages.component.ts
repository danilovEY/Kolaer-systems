import {Component, OnInit} from '@angular/core';

import {NbMenuItem} from '@nebular/theme';
import {AuthenticationRestService} from '../@core/modules/auth/authentication-rest.service';
import {AccountService} from "../@core/services/account.service";
import {SimpleAccountModel} from "../@core/models/simple-account.model";

@Component({
    selector: 'ngx-pages',
    template: `
        <default-layout>
            <nb-menu [items]="menu"></nb-menu>
            <router-outlet></router-outlet>
        </default-layout>
    `,
})
export class PagesComponent implements OnInit {
    menu: NbMenuItem[] = [];

    constructor(private authService: AuthenticationRestService,
                private accountService: AccountService) {
    }

    ngOnInit() {
        const dashboardMenuItem: NbMenuItem = new NbMenuItem();
        dashboardMenuItem.title = 'Домашняя страница';
        dashboardMenuItem.icon = 'nb-home';
        dashboardMenuItem.link = '/pages/dashboard';
        dashboardMenuItem.home = true;


        const appMenuMenuItem: NbMenuItem = new NbMenuItem();
        appMenuMenuItem.title = 'Приложения';
        appMenuMenuItem.group = true;

        this.menu.push(dashboardMenuItem, appMenuMenuItem);

        if (this.authService.authentication) {
            const kolpassMenuItem: NbMenuItem = new NbMenuItem();
            kolpassMenuItem.title = 'Парольница';
            kolpassMenuItem.icon = 'icon ion-md-lock';
            kolpassMenuItem.children = [];

            const kolpassMainMenuItem: NbMenuItem = new NbMenuItem();
            kolpassMainMenuItem.title = 'Главная';
            kolpassMainMenuItem.link = 'app/kolpass';

            kolpassMenuItem.children.push(kolpassMainMenuItem);
            this.menu.push(kolpassMenuItem);


            const ticketsMenuItem: NbMenuItem = new NbMenuItem();
            ticketsMenuItem.title = 'Талоны ЛПП';
            ticketsMenuItem.icon = 'fa fa-ticket';
            ticketsMenuItem.children = [];

            const ticketsMainMenuItem: NbMenuItem = new NbMenuItem();
            ticketsMainMenuItem.title = 'Главная';
            ticketsMainMenuItem.link = 'app/tickets';

            this.accountService.getCurrentAccount()
                .subscribe((account: SimpleAccountModel) => {
                   if (account.accessOit) {
                       const bankAccountMenuItem: NbMenuItem = new NbMenuItem();
                       bankAccountMenuItem.title = 'Счета';
                       bankAccountMenuItem.link = 'app/tickets/bank-accounts';

                       ticketsMenuItem.children.push(bankAccountMenuItem);
                   }
                });

            ticketsMenuItem.children.push(ticketsMainMenuItem);
            this.menu.push(ticketsMenuItem);


            const orgStructureMenuItem: NbMenuItem = new NbMenuItem();
            orgStructureMenuItem.title = 'Структура организации';
            orgStructureMenuItem.icon = 'icon ion-md-business';
            orgStructureMenuItem.children = [];

            const employeesMenuItem: NbMenuItem = new NbMenuItem();
            employeesMenuItem.title = 'Сотрудники';
            employeesMenuItem.link = 'app/org-structures/employees';

            orgStructureMenuItem.children.push(employeesMenuItem);
            this.menu.push(orgStructureMenuItem);
        }

    }

}

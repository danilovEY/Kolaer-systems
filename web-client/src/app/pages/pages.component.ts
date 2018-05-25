import {Component, OnInit} from '@angular/core';

import {NbMenuItem} from '@nebular/theme';
import {AuthenticationRestService} from '../@core/modules/auth/authentication-rest.service';

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

    constructor(private authService: AuthenticationRestService) {

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
            kolpassMenuItem.icon = 'nb-locked';
            kolpassMenuItem.children = [];

            const kolpassMainMenuItem: NbMenuItem = new NbMenuItem();
            kolpassMainMenuItem.title = 'Главная';
            kolpassMainMenuItem.link = 'app/kolpass';

            kolpassMenuItem.children.push(kolpassMainMenuItem);

            this.menu.push(kolpassMenuItem);


            const ticketsMenuItem: NbMenuItem = new NbMenuItem();
            ticketsMenuItem.title = 'Парольница';
            ticketsMenuItem.icon = 'ticket-alt';
            ticketsMenuItem.children = [];

            const ticketsMainMenuItem: NbMenuItem = new NbMenuItem();
            ticketsMainMenuItem.title = 'Главная';
            ticketsMainMenuItem.link = 'app/tickets';

            ticketsMenuItem.children.push(ticketsMainMenuItem);

            this.menu.push(ticketsMenuItem);
        }

    }

}

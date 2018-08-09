import {Component, OnInit} from '@angular/core';

import {NbMenuItem} from '@nebular/theme';
import {AuthenticationRestService} from '../@core/modules/auth/authentication-rest.service';
import {AccountService} from '../@core/services/account.service';
import {SimpleAccountModel} from '../@core/models/simple-account.model';

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

        // ======= Контакты ======

        const contactsMenuItem: NbMenuItem = new NbMenuItem();
        contactsMenuItem.title = 'Контакты';
        contactsMenuItem.icon = 'fa fa-address-book';
        contactsMenuItem.children = [];

        const contactsMainMenuItem: NbMenuItem = new NbMenuItem();
        contactsMainMenuItem.title = 'Главная';
        contactsMainMenuItem.link = 'app/contacts';

        contactsMenuItem.children.push(contactsMainMenuItem);
        this.menu.push(contactsMenuItem);

        // ======= Производственный календарь ======

        const productionCalendarMenuItem: NbMenuItem = new NbMenuItem();
        productionCalendarMenuItem.title = 'Производственный календарь';
        productionCalendarMenuItem.icon = 'icon ion-md-calendar';
        productionCalendarMenuItem.children = [];

        const productionCalendarMainMenuItem: NbMenuItem = new NbMenuItem();
        productionCalendarMainMenuItem.title = 'Главная';
        productionCalendarMainMenuItem.link = 'app/production-calendar/main';

        productionCalendarMenuItem.children.push(productionCalendarMainMenuItem);
        this.menu.push(productionCalendarMenuItem);


        if (this.authService.authentication) {
            // ======= Парольница ======
            const kolpassMenuItem: NbMenuItem = new NbMenuItem();
            kolpassMenuItem.title = 'Парольница';
            kolpassMenuItem.icon = 'icon ion-md-lock';
            kolpassMenuItem.children = [];

            const kolpassMainMenuItem: NbMenuItem = new NbMenuItem();
            kolpassMainMenuItem.title = 'Главная';
            kolpassMainMenuItem.link = 'app/kolpass';

            kolpassMenuItem.children.push(kolpassMainMenuItem);
            this.menu.push(kolpassMenuItem);

            // ======= Очередь ======
            const queueMenuItem: NbMenuItem = new NbMenuItem();
            queueMenuItem.title = 'Очередь';
            queueMenuItem.icon = 'icon ion-md-git-pull-request';
            queueMenuItem.children = [];

            const queueMainMenuItem: NbMenuItem = new NbMenuItem();
            queueMainMenuItem.title = 'Главная';
            queueMainMenuItem.link = 'app/queue';

            const queueTargetMenuItem: NbMenuItem = new NbMenuItem();
            queueTargetMenuItem.title = 'Цели';
            queueTargetMenuItem.link = 'app/queue/target';

            queueMenuItem.children.push(queueMainMenuItem, queueTargetMenuItem);
            this.menu.push(queueMenuItem);

            // ======= Талоны ЛПП ======

            const ticketsMenuItem: NbMenuItem = new NbMenuItem();
            ticketsMenuItem.title = 'Талоны ЛПП';
            ticketsMenuItem.icon = 'fa fa-ticket';
            ticketsMenuItem.children = [];

            const ticketsMainMenuItem: NbMenuItem = new NbMenuItem();
            ticketsMainMenuItem.title = 'Главная';
            ticketsMainMenuItem.link = 'app/tickets';

            // ======= Структура организации ======

            const orgStructureMenuItem: NbMenuItem = new NbMenuItem();
            orgStructureMenuItem.title = 'Структура организации';
            orgStructureMenuItem.icon = 'icon ion-md-business';
            orgStructureMenuItem.children = [];

            const employeesMenuItem: NbMenuItem = new NbMenuItem();
            employeesMenuItem.title = 'Сотрудники';
            employeesMenuItem.link = 'app/org-structures/employees';

            const postsMenuItem: NbMenuItem = new NbMenuItem();
            postsMenuItem.title = 'Должности';
            postsMenuItem.link = 'app/org-structures/posts';

            const departmentsMenuItem: NbMenuItem = new NbMenuItem();
            departmentsMenuItem.title = 'Подразделения';
            departmentsMenuItem.link = 'app/org-structures/departments';

            orgStructureMenuItem.children.push(employeesMenuItem,
                postsMenuItem,
                departmentsMenuItem);
            this.menu.push(orgStructureMenuItem);

            this.accountService.getCurrentAccount()
                .subscribe((account: SimpleAccountModel) => {
                   if (account.accessOit) {
                       const bankAccountMenuItem: NbMenuItem = new NbMenuItem();
                       bankAccountMenuItem.title = 'Счета';
                       bankAccountMenuItem.link = 'app/tickets/bank-accounts';

                       ticketsMenuItem.children.push(bankAccountMenuItem);

                       const productionCalendarEditMenuItem: NbMenuItem = new NbMenuItem();
                       productionCalendarEditMenuItem.title = 'Редактировать';
                       productionCalendarEditMenuItem.link = 'app/production-calendar/edit';

                       productionCalendarMenuItem.children.push(productionCalendarEditMenuItem);

                       const orgStructureSyncMenuItem: NbMenuItem = new NbMenuItem();
                       orgStructureSyncMenuItem.title = 'Синхронизация';
                       orgStructureSyncMenuItem.link = 'app/org-structures/sync';

                       orgStructureMenuItem.children.push(orgStructureSyncMenuItem);
                   }
                });

            ticketsMenuItem.children.push(ticketsMainMenuItem);
            this.menu.push(ticketsMenuItem);
        }

    }

}

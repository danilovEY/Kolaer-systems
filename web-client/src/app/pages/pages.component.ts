import {Component, OnInit} from '@angular/core';

import {NbMenuItem} from '@nebular/theme';
import {AuthenticationRestService} from '../@core/modules/auth/authentication-rest.service';
import {AccountService} from '../@core/services/account.service';
import {SimpleAccountModel} from '../@core/models/simple-account.model';
import {RoleConstant} from "../@core/constants/role.constant";

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


        // ======= Контакты ======
        const contactsMenuItem: NbMenuItem = new NbMenuItem();
        contactsMenuItem.title = 'Контакты';
        contactsMenuItem.icon = 'fa fa-address-book';
        contactsMenuItem.children = [];

        const contactsMainMenuItem: NbMenuItem = new NbMenuItem();
        contactsMainMenuItem.title = 'Главная';
        contactsMainMenuItem.link = 'app/contacts';

        contactsMenuItem.children.push(contactsMainMenuItem);



        // ======= Производственный календарь ======
        const productionCalendarMenuItem: NbMenuItem = new NbMenuItem();
        productionCalendarMenuItem.title = 'Производственный календарь';
        productionCalendarMenuItem.icon = 'icon ion-md-calendar';
        productionCalendarMenuItem.children = [];

        const productionCalendarMainMenuItem: NbMenuItem = new NbMenuItem();
        productionCalendarMainMenuItem.title = 'Главная';
        productionCalendarMainMenuItem.link = 'app/production-calendar/main';

        const productionCalendarEditMenuItem: NbMenuItem = new NbMenuItem();
        productionCalendarEditMenuItem.title = 'Редактировать';
        productionCalendarEditMenuItem.link = 'app/production-calendar/edit';

        productionCalendarMenuItem.children.push(productionCalendarMainMenuItem);



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

        const typeWorkMenuItem: NbMenuItem = new NbMenuItem();
        typeWorkMenuItem.title = 'Вид работ';
        typeWorkMenuItem.link = 'app/org-structures/type-work';

        const orgStructureSyncMenuItem: NbMenuItem = new NbMenuItem();
        orgStructureSyncMenuItem.title = 'Синхронизация';
        orgStructureSyncMenuItem.link = 'app/org-structures/sync';



        // ======= График отпусков ======
        const vacationMenuItem: NbMenuItem = new NbMenuItem();
        vacationMenuItem.title = 'График отпусков';
        vacationMenuItem.icon = 'icon ion-md-airplane';
        vacationMenuItem.children = [];

        const vacationMainMenuItem: NbMenuItem = new NbMenuItem();
        vacationMainMenuItem.title = 'Главная';
        vacationMainMenuItem.link = 'app/vacation/main';

        const vacationSetMenuItem: NbMenuItem = new NbMenuItem();
        vacationSetMenuItem.title = 'Задать отпуск';
        vacationSetMenuItem.link = 'app/vacation/set';

        const reportExportMenuItem: NbMenuItem = new NbMenuItem();
        reportExportMenuItem.title = 'Отчет';
        reportExportMenuItem.link = 'app/vacation/report/export';

        const vacationReportMenuItem: NbMenuItem = new NbMenuItem();
        vacationReportMenuItem.title = 'Графики';
        vacationReportMenuItem.icon = 'icon ion-md-analytics';
        vacationReportMenuItem.children = [];

        const vacationReportDistributedMenuItem: NbMenuItem = new NbMenuItem();
        vacationReportDistributedMenuItem.title = 'График распределений';
        vacationReportDistributedMenuItem.link = 'app/vacation/report/distribute';

        const vacationReportCalendarMenuItem: NbMenuItem = new NbMenuItem();
        vacationReportCalendarMenuItem.title = 'График пересечений';
        vacationReportCalendarMenuItem.link = 'app/vacation/report/calendar';

        const vacationReportTotalCountMenuItem: NbMenuItem = new NbMenuItem();
        vacationReportTotalCountMenuItem.title = 'График соотношений';
        vacationReportTotalCountMenuItem.link = 'app/vacation/report/total-count';


        vacationReportMenuItem.children.push(vacationReportDistributedMenuItem,
            vacationReportCalendarMenuItem,
            vacationReportTotalCountMenuItem);

        vacationMenuItem.children.push(vacationMainMenuItem);


        // ======= Парольница ======
        const kolpassMenuItem: NbMenuItem = new NbMenuItem();
        kolpassMenuItem.title = 'Парольница';
        kolpassMenuItem.icon = 'icon ion-md-lock';
        kolpassMenuItem.children = [];

        const kolpassMainMenuItem: NbMenuItem = new NbMenuItem();
        kolpassMainMenuItem.title = 'Главная';
        kolpassMainMenuItem.link = 'app/kolpass';

        kolpassMenuItem.children.push(kolpassMainMenuItem);


        // ======= Очередь ======
        const queueMenuItem: NbMenuItem = new NbMenuItem();
        queueMenuItem.title = 'Бронирование кабинетов';
        queueMenuItem.icon = 'icon ion-md-git-pull-request';
        queueMenuItem.children = [];

        const queueMainMenuItem: NbMenuItem = new NbMenuItem();
        queueMainMenuItem.title = 'Главная';
        queueMainMenuItem.link = 'app/queue';

        const queueTargetMenuItem: NbMenuItem = new NbMenuItem();
        queueTargetMenuItem.title = 'Бронирование';
        queueTargetMenuItem.link = 'app/queue/target';

        queueMenuItem.children.push(queueMainMenuItem, queueTargetMenuItem);


        // ======= Талоны ЛПП ======
        const ticketsMenuItem: NbMenuItem = new NbMenuItem();
        ticketsMenuItem.title = 'Талоны ЛПП';
        ticketsMenuItem.icon = 'fas fa-ticket-alt';
        ticketsMenuItem.children = [];

        const ticketsMainMenuItem: NbMenuItem = new NbMenuItem();
        ticketsMainMenuItem.title = 'Главная';
        ticketsMainMenuItem.link = 'app/tickets';

        const bankAccountMenuItem: NbMenuItem = new NbMenuItem();
        bankAccountMenuItem.title = 'Счета';
        bankAccountMenuItem.link = 'app/tickets/bank-accounts';

        // =======График командировок ========
        const businessTripMenuItem: NbMenuItem = new NbMenuItem();
        businessTripMenuItem.title = 'График командировок';
        businessTripMenuItem.icon = 'fas fa-business-time';
        businessTripMenuItem.children = [];

        const businessTripListMenuItem: NbMenuItem = new NbMenuItem();
        businessTripListMenuItem.title = 'Список';
        businessTripListMenuItem.link = 'app/business-trip/list';



        this.menu.push(dashboardMenuItem, appMenuMenuItem);
        this.menu.push(contactsMenuItem);
        this.menu.push(productionCalendarMenuItem);

        if (this.authService.authentication) {
            ticketsMenuItem.children.push(ticketsMainMenuItem);

            this.menu.push(vacationMenuItem);
            this.menu.push(kolpassMenuItem);
            this.menu.push(queueMenuItem);
            this.menu.push(ticketsMenuItem);

            this.accountService.getCurrentAccount()
                .subscribe((account: SimpleAccountModel) => {
                    if (account.access.includes(RoleConstant.EMPLOYEES_READ) ||
                        account.access.includes(RoleConstant.EMPLOYEES_READ_DEPARTMENT)
                    ) {
                        orgStructureMenuItem.children.push(employeesMenuItem);
                    }

                    orgStructureMenuItem.children.push(departmentsMenuItem);
                    orgStructureMenuItem.children.push(postsMenuItem);

                    if (account.access.includes(RoleConstant.TYPE_WORKS_READ)) {
                        orgStructureMenuItem.children.push(typeWorkMenuItem);
                    }

                    if (orgStructureMenuItem.children.length > 0) {
                        this.menu.push(orgStructureMenuItem);
                    }

                    if (account.access.includes(RoleConstant.BANK_ACCOUNTS_READ)) {
                        ticketsMenuItem.children.push(bankAccountMenuItem);
                    }

                    if (account.access.includes(RoleConstant.EMPLOYEES_SYNC)) {
                        orgStructureMenuItem.children.push(orgStructureSyncMenuItem);
                    }


                    if (account.access.includes(RoleConstant.VACATIONS_READ) ||
                        account.access.includes(RoleConstant.VACATIONS_READ_DEPARTMENT)) {
                        vacationMenuItem.children.push(vacationSetMenuItem,
                            reportExportMenuItem,
                            vacationReportMenuItem);
                    }

                    if (account.access.includes(RoleConstant.BUSINESS_TRIP_READ)) {
                        businessTripMenuItem.children.push(businessTripListMenuItem);

                        // this.menu.push(businessTripMenuItem); TODO complete app
                    }

                    // productionCalendarMenuItem.children.push(productionCalendarEditMenuItem); TODO: add role
                });
        }
    }

}

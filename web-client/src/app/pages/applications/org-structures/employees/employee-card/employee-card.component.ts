import {Component, OnDestroy, OnInit} from '@angular/core';
import {NbMenuItem, NbMenuService} from "@nebular/theme";
import {AccountService} from '../../../../../@core/services/account.service';
import {ActivatedRoute, Router} from "@angular/router";
import {Subject} from "rxjs";
import {PathVariableConstant} from "../../../../../@core/constants/path-variable.constant";
import {RouterConstant} from "../../../../../@core/constants/router.constant";

@Component({
    selector: 'employee-card',
    templateUrl: './employee-card.component.html',
    styleUrls: ['./employee-card.component.scss']
})
export class EmployeeCardComponent implements OnInit, OnDestroy {
    private readonly ngUnsubscribe = new Subject();

    readonly employeeInfoMenu: NbMenuItem[] = [];

    private employeeId: number;

    constructor(private nbMenuService: NbMenuService,
                private accountService: AccountService,
                private router: Router,
                private activatedRoute: ActivatedRoute) {

    }

    ngOnInit(): void {
        this.employeeId = this.activatedRoute.parent.snapshot.params[PathVariableConstant.EMPLOYEE_ID];

        const commonMenuItem: NbMenuItem = new NbMenuItem();
        commonMenuItem.title = 'Общая информация';
        commonMenuItem.home = true;
        commonMenuItem.link = RouterConstant.createUrlFromUrlTemplate(
            RouterConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_COMMONS_URL,
            PathVariableConstant.EMPLOYEE_ID,
            this.employeeId.toString()
        );

        const educationsMenuItem: NbMenuItem = new NbMenuItem();
        educationsMenuItem.title = 'Образование';
        educationsMenuItem.link = RouterConstant.createUrlFromUrlTemplate(
            RouterConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_EDUCATIONS_URL,
            PathVariableConstant.EMPLOYEE_ID,
            this.employeeId.toString()
        );

        const achievementsMenuItem: NbMenuItem = new NbMenuItem();
        achievementsMenuItem.title = 'Достижения';
        achievementsMenuItem.link = RouterConstant.createUrlFromUrlTemplate(
            RouterConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_ACHIEVEMENTS_URL,
            PathVariableConstant.EMPLOYEE_ID,
            this.employeeId.toString()
        );

        const punishmentsMenuItem: NbMenuItem = new NbMenuItem();
        punishmentsMenuItem.title = 'Взыскания';
        punishmentsMenuItem.link = RouterConstant.createUrlFromUrlTemplate(
            RouterConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_PUNISHMENTS_URL,
            PathVariableConstant.EMPLOYEE_ID,
            this.employeeId.toString()
        );

        const employmentHistoriesMenuItem: NbMenuItem = new NbMenuItem();
        employmentHistoriesMenuItem.title = 'Трудовая книжка';
        employmentHistoriesMenuItem.link = RouterConstant.createUrlFromUrlTemplate(
            RouterConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_EMPLOYMENT_HISTORIES_URL,
            PathVariableConstant.EMPLOYEE_ID,
            this.employeeId.toString()
        );

        const staffMovementsMenuItem: NbMenuItem = new NbMenuItem();
        staffMovementsMenuItem.title = 'Движение персонала';
        staffMovementsMenuItem.link = RouterConstant.createUrlFromUrlTemplate(
            RouterConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_STAFF_MOVEMENTS_URL,
            PathVariableConstant.EMPLOYEE_ID,
            this.employeeId.toString()
        );

        const combinationsMenuItem: NbMenuItem = new NbMenuItem();
        combinationsMenuItem.title = 'Совмещения';
        combinationsMenuItem.link = RouterConstant.createUrlFromUrlTemplate(
            RouterConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_COMBINATIONS_URL,
            PathVariableConstant.EMPLOYEE_ID,
            this.employeeId.toString()
        );

        const vacationsMenuItem: NbMenuItem = new NbMenuItem();
        vacationsMenuItem.title = 'Планируемые отпуска';
        vacationsMenuItem.link = RouterConstant.createUrlFromUrlTemplate(
            RouterConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_VACATIONS_URL,
            PathVariableConstant.EMPLOYEE_ID,
            this.employeeId.toString()
        );

        const personalDataMenuItem: NbMenuItem = new NbMenuItem();
        personalDataMenuItem.title = 'Персональные данные';
        personalDataMenuItem.link = RouterConstant.createUrlFromUrlTemplate(
            RouterConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_PERSONAL_DATA_URL,
            PathVariableConstant.EMPLOYEE_ID,
            this.employeeId.toString()
        );

        const relativesMenuItem: NbMenuItem = new NbMenuItem();
        relativesMenuItem.title = 'Родственники';
        relativesMenuItem.link = RouterConstant.createUrlFromUrlTemplate(
            RouterConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_RELATIVES_URL,
            PathVariableConstant.EMPLOYEE_ID,
            this.employeeId.toString()
        );

        const personalDocumentsMenuItem: NbMenuItem = new NbMenuItem();
        personalDocumentsMenuItem.title = 'Персональные документы';
        personalDocumentsMenuItem.link = RouterConstant.createUrlFromUrlTemplate(
            RouterConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_PERSONAL_DOCUMENTS_URL,
            PathVariableConstant.EMPLOYEE_ID,
            this.employeeId.toString()
        );

        this.employeeInfoMenu.push(
            commonMenuItem,
            educationsMenuItem,
            achievementsMenuItem,
            punishmentsMenuItem,
            employmentHistoriesMenuItem,
            staffMovementsMenuItem,
            combinationsMenuItem,
            vacationsMenuItem,
            personalDataMenuItem,
            relativesMenuItem,
            personalDocumentsMenuItem
        );
    }

    ngOnDestroy(): void {
        this.ngUnsubscribe.next(true);
    }
}

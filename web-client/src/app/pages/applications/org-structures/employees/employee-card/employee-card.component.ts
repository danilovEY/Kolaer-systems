import {Component, OnDestroy, OnInit} from '@angular/core';
import {NbMenuItem, NbMenuService} from "@nebular/theme";
import {AccountService} from '../../../../../@core/services/account.service';
import {ActivatedRoute, Router} from "@angular/router";
import {Subject} from "rxjs";
import {PathVariableConstant} from "../../../../../@core/constants/path-variable.constant";
import {RouterClientConstant} from "../../../../../@core/constants/router-client.constant";
import {takeUntil} from "rxjs/operators";
import {EmployeeCardService} from "./employee-card.service";
import {Utils} from "../../../../../@core/utils/utils";

@Component({
    selector: 'employee-card',
    templateUrl: './employee-card.component.html',
    styleUrls: ['./employee-card.component.scss']
})
export class EmployeeCardComponent implements OnInit, OnDestroy {
    private readonly ngUnsubscribe = new Subject();

    employeeInfoMenu: NbMenuItem[] = [];

    constructor(private nbMenuService: NbMenuService,
                private accountService: AccountService,
                private router: Router,
                private employeeCardService: EmployeeCardService,
                private activatedRoute: ActivatedRoute) {

    }

    ngOnInit(): void {

        this.activatedRoute.parent.params
            .pipe(takeUntil(this.ngUnsubscribe))
            .subscribe(params => {
                const employeeId: number = params[PathVariableConstant.EMPLOYEE_ID];

                if (!isNaN(employeeId)) {
                    this.employeeCardService.setSelectedEmployeeId(employeeId);

                    this.initMenuItems(employeeId);
                } else {
                    this.router.navigate(['/']);
                }
        });
    }

    initMenuItems(employeeId: number) {
        this.employeeInfoMenu = [];

        const commonMenuItem: NbMenuItem = new NbMenuItem();
        commonMenuItem.title = 'Общая информация';
        commonMenuItem.home = true;
        commonMenuItem.link = Utils.createUrlFromUrlTemplate(
            RouterClientConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_COMMONS_URL,
            PathVariableConstant.EMPLOYEE_ID,
            employeeId.toString()
        );

        const educationsMenuItem: NbMenuItem = new NbMenuItem();
        educationsMenuItem.title = 'Образование';
        educationsMenuItem.link = Utils.createUrlFromUrlTemplate(
            RouterClientConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_EDUCATIONS_URL,
            PathVariableConstant.EMPLOYEE_ID,
            employeeId.toString()
        );

        const achievementsMenuItem: NbMenuItem = new NbMenuItem();
        achievementsMenuItem.title = 'Достижения';
        achievementsMenuItem.link = Utils.createUrlFromUrlTemplate(
            RouterClientConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_ACHIEVEMENTS_URL,
            PathVariableConstant.EMPLOYEE_ID,
            employeeId.toString()
        );

        const punishmentsMenuItem: NbMenuItem = new NbMenuItem();
        punishmentsMenuItem.title = 'Взыскания';
        punishmentsMenuItem.link = Utils.createUrlFromUrlTemplate(
            RouterClientConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_PUNISHMENTS_URL,
            PathVariableConstant.EMPLOYEE_ID,
            employeeId.toString()
        );

        const employmentHistoriesMenuItem: NbMenuItem = new NbMenuItem();
        employmentHistoriesMenuItem.title = 'Трудовая книжка';
        employmentHistoriesMenuItem.link = Utils.createUrlFromUrlTemplate(
            RouterClientConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_EMPLOYMENT_HISTORIES_URL,
            PathVariableConstant.EMPLOYEE_ID,
            employeeId.toString()
        );

        const staffMovementsMenuItem: NbMenuItem = new NbMenuItem();
        staffMovementsMenuItem.title = 'Движение персонала';
        staffMovementsMenuItem.link = Utils.createUrlFromUrlTemplate(
            RouterClientConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_STAFF_MOVEMENTS_URL,
            PathVariableConstant.EMPLOYEE_ID,
            employeeId.toString()
        );

        const combinationsMenuItem: NbMenuItem = new NbMenuItem();
        combinationsMenuItem.title = 'Совмещения';
        combinationsMenuItem.link = Utils.createUrlFromUrlTemplate(
            RouterClientConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_COMBINATIONS_URL,
            PathVariableConstant.EMPLOYEE_ID,
            employeeId.toString()
        );

        const vacationsMenuItem: NbMenuItem = new NbMenuItem();
        vacationsMenuItem.title = 'Планируемые отпуска';
        vacationsMenuItem.link = Utils.createUrlFromUrlTemplate(
            RouterClientConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_VACATIONS_URL,
            PathVariableConstant.EMPLOYEE_ID,
            employeeId.toString()
        );

        const personalDataMenuItem: NbMenuItem = new NbMenuItem();
        personalDataMenuItem.title = 'Персональные данные';
        personalDataMenuItem.link = Utils.createUrlFromUrlTemplate(
            RouterClientConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_PERSONAL_DATA_URL,
            PathVariableConstant.EMPLOYEE_ID,
            employeeId.toString()
        );

        const relativesMenuItem: NbMenuItem = new NbMenuItem();
        relativesMenuItem.title = 'Родственники';
        relativesMenuItem.link = Utils.createUrlFromUrlTemplate(
            RouterClientConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_RELATIVES_URL,
            PathVariableConstant.EMPLOYEE_ID,
            employeeId.toString()
        );

        const personalDocumentsMenuItem: NbMenuItem = new NbMenuItem();
        personalDocumentsMenuItem.title = 'Персональные документы';
        personalDocumentsMenuItem.link = Utils.createUrlFromUrlTemplate(
            RouterClientConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_PERSONAL_DOCUMENTS_URL,
            PathVariableConstant.EMPLOYEE_ID,
            employeeId.toString()
        );

        const militaryRegistrationMenuItem: NbMenuItem = new NbMenuItem();
        militaryRegistrationMenuItem.title = 'Воинский учёт';
        militaryRegistrationMenuItem.link = Utils.createUrlFromUrlTemplate(
            RouterClientConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_MILITARY_REGISTRATION_URL,
            PathVariableConstant.EMPLOYEE_ID,
            employeeId.toString()
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
            personalDocumentsMenuItem,
            militaryRegistrationMenuItem
        );
    }

    ngOnDestroy(): void {
        this.ngUnsubscribe.next(true);
        this.ngUnsubscribe.complete();
    }
}

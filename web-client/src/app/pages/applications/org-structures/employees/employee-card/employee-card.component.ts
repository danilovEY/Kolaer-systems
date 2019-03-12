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
import {SimpleAccountModel} from "../../../../../@core/models/simple-account.model";
import {RoleConstant} from "../../../../../@core/constants/role.constant";
import {EmployeeService} from "../../../../../@core/services/employee.service";
import {EmployeeModel} from "../../../../../@core/models/employee.model";

@Component({
    selector: 'employee-card',
    templateUrl: './employee-card.component.html',
    styleUrls: ['./employee-card.component.scss']
})
export class EmployeeCardComponent implements OnInit, OnDestroy {
    private readonly ngUnsubscribe = new Subject();

    employeeInfoMenu: NbMenuItem[] = [];
    selectedEmployee: EmployeeModel;

    constructor(private nbMenuService: NbMenuService,
                private accountService: AccountService,
                private router: Router,
                private employeeService: EmployeeService,
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

                    this.employeeService.getEmployeeById(employeeId)
                        .subscribe((employee: EmployeeModel) => this.selectedEmployee = employee);

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

        this.accountService.getCurrentAccount()
            .pipe(takeUntil(this.ngUnsubscribe))
            .subscribe((account: SimpleAccountModel) => {
                this.employeeInfoMenu.push(commonMenuItem);

                if (account.access.includes(RoleConstant.EMPLOYEE_EDUCATIONS_READ) ||
                    account.access.includes(RoleConstant.EMPLOYEE_EDUCATIONS_READ_DEPARTMENT)) {
                    this.employeeInfoMenu.push(educationsMenuItem);
                }

                if (account.access.includes(RoleConstant.EMPLOYEE_ACHIEVEMENTS_READ) ||
                    account.access.includes(RoleConstant.EMPLOYEE_ACHIEVEMENTS_READ_DEPARTMENT)) {
                    this.employeeInfoMenu.push(achievementsMenuItem);
                }

                if (account.access.includes(RoleConstant.EMPLOYEE_PUNISHMENTS_READ) ||
                    account.access.includes(RoleConstant.EMPLOYEE_PUNISHMENTS_READ_DEPARTMENT)) {
                    this.employeeInfoMenu.push(punishmentsMenuItem);
                }

                if (account.access.includes(RoleConstant.EMPLOYEE_EMPLOYMENT_HISTORIES_READ) ||
                    account.access.includes(RoleConstant.EMPLOYEE_EMPLOYMENT_HISTORIES_READ_DEPARTMENT)) {
                    this.employeeInfoMenu.push(employmentHistoriesMenuItem);
                }

                if (account.access.includes(RoleConstant.EMPLOYEE_STAFF_MOVEMENTS_READ) ||
                    account.access.includes(RoleConstant.EMPLOYEE_STAFF_MOVEMENTS_READ_DEPARTMENT)) {
                    this.employeeInfoMenu.push(staffMovementsMenuItem);
                }

                if (account.access.includes(RoleConstant.EMPLOYEE_COMBINATION_READ) ||
                    account.access.includes(RoleConstant.EMPLOYEE_COMBINATION_READ_DEPARTMENT)) {
                    this.employeeInfoMenu.push(combinationsMenuItem);
                }

                // if (account.access.includes(RoleConstant.EMPLOYEE_EDUCATIONS_READ) || TODO complete
                //     account.access.includes(RoleConstant.EMPLOYEE_EDUCATIONS_READ_DEPARTMENT) {
                //     this.employeeInfoMenu.push(vacationsMenuItem);
                // }

                if (account.access.includes(RoleConstant.EMPLOYEE_PERSONAL_DATA_READ) ||
                    account.access.includes(RoleConstant.EMPLOYEE_PERSONAL_DATA_READ_DEPARTMENT)) {
                    this.employeeInfoMenu.push(personalDataMenuItem);
                }

                if (account.access.includes(RoleConstant.EMPLOYEE_RELATIVES_READ) ||
                    account.access.includes(RoleConstant.EMPLOYEE_RELATIVES_READ_DEPARTMENT)) {
                    this.employeeInfoMenu.push(relativesMenuItem);
                }

                if (account.access.includes(RoleConstant.EMPLOYEE_PERSONAL_DOCUMENT_READ) ||
                    account.access.includes(RoleConstant.EMPLOYEE_PERSONAL_DOCUMENT_READ_DEPARTMENT)) {
                    this.employeeInfoMenu.push(personalDocumentsMenuItem);
                }

                if (account.access.includes(RoleConstant.EMPLOYEE_MILITARY_REGISTRATIONS_READ) ||
                    account.access.includes(RoleConstant.EMPLOYEE_MILITARY_REGISTRATIONS_READ_DEPARTMENT)) {
                    this.employeeInfoMenu.push(militaryRegistrationMenuItem);
                }

                this.employeeInfoMenu.sort((a, b) => a.title > b.title ? 1 : -1);
            });
    }

    ngOnDestroy(): void {
        this.ngUnsubscribe.next(true);
        this.ngUnsubscribe.complete();
    }
}

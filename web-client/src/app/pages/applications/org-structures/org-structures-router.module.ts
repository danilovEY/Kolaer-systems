import {RouterModule, Routes} from '@angular/router';
import {NgModule} from '@angular/core';
import {EmployeesListComponent} from './employees/list/employees-list.component';
import {DepartmentsComponent} from './departments/departments.component';
import {PostsComponent} from './posts/posts.component';
import {OrgStructuresSyncComponent} from './synch/org-structures-sync.component';
import {TypeWorkComponent} from './type-work/type-work.component';
import {EmployeeCardComponent} from './employees/employee-card/employee-card.component';
import {EmployeesComponent} from './employees/employees.component';
import {EmployeeCardCommonsComponent} from './employees/employee-card/commons/employee-card-commons.component';
import {EmployeeCardEducationsComponent} from './employees/employee-card/educations/employee-card-educations.component';
import {RouterClientConstant} from "../../../@core/constants/router-client.constant";
import {EmptyRouterComponent} from "../../../@theme/components/empty-router.component";
import {EmployeeCardAchievementsComponent} from "./employees/employee-card/achievements/employee-card-achievements.component";
import {EmployeeCardPunishmentsComponent} from "./employees/employee-card/punishments/employee-card-punishments.component";
import {EmployeeCardEmploymentHistoriesComponent} from "./employees/employee-card/employment-histories/employee-card-employment-histories.component"; // tslint:disable
import {EmployeeCardStaffMovementsComponent} from "./employees/employee-card/staff-movements/employee-card-staff-movements.component";
import {EmployeeCardCombinationsComponent} from "./employees/employee-card/combinations/employee-card-combinations.component";
import {EmployeeCardVacationsComponent} from "./employees/employee-card/vacations/employee-card-vacations.component";
import {EmployeeCardPersonalDataComponent} from "./employees/employee-card/personal-data/employee-card-personal-data.component";
import {EmployeeCardRelativesComponent} from "./employees/employee-card/relatives/employee-card-relatives.component";
import {EmployeeCardPersonalDocumentsComponent} from "./employees/employee-card/personal-documents/employee-card-personal-documents.component";
import {EmployeeCardMilitaryRegistrationComponent} from "./employees/employee-card/military-registration/employee-card-military-registration.component";


const routes: Routes = [
    {path: 'departments', component: DepartmentsComponent},
    {path: 'posts', component: PostsComponent},
    {path: 'sync', component: OrgStructuresSyncComponent},
    {path: 'type-work', component: TypeWorkComponent},
    {
        path: RouterClientConstant.ORG_STRUCTURES_EMPLOYEES_PART_URL, component: EmployeesComponent,
        children: [
            {path: '', redirectTo: 'list'},
            {path: 'list', component: EmployeesListComponent},
            {
                path: RouterClientConstant.ORG_STRUCTURES_EMPLOYEES_ID_PART_URL, component: EmptyRouterComponent,
                children: [
                    {path: '', redirectTo: RouterClientConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_PART_URL},
                    {
                        path: RouterClientConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_PART_URL,
                        component: EmployeeCardComponent,
                        children: [
                            {path: '', redirectTo: RouterClientConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_COMMONS_PART_URL},
                            {
                                path: RouterClientConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_COMMONS_PART_URL,
                                component: EmployeeCardCommonsComponent
                            },
                            {
                                path: RouterClientConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_EDUCATIONS_PART_URL,
                                component: EmployeeCardEducationsComponent
                            },
                            {
                                path: RouterClientConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_ACHIEVEMENTS_PART_URL,
                                component: EmployeeCardAchievementsComponent
                            },
                            {
                                path: RouterClientConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_PUNISHMENTS_PART_URL,
                                component: EmployeeCardPunishmentsComponent
                            },
                            {
                                path: RouterClientConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_EMPLOYMENT_HISTORIES_PART_URL,
                                component: EmployeeCardEmploymentHistoriesComponent
                            },
                            {
                                path: RouterClientConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_STAFF_MOVEMENTS_PART_URL,
                                component: EmployeeCardStaffMovementsComponent
                            },
                            {
                                path: RouterClientConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_COMBINATIONS_PART_URL,
                                component: EmployeeCardCombinationsComponent
                            },
                            {
                                path: RouterClientConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_VACATIONS_PART_URL,
                                component: EmployeeCardVacationsComponent
                            },
                            {
                                path: RouterClientConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_PERSONAL_DATA_PART_URL,
                                component: EmployeeCardPersonalDataComponent
                            },
                            {
                                path: RouterClientConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_RELATIVES_PART_URL,
                                component: EmployeeCardRelativesComponent
                            },
                            {
                                path: RouterClientConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_PERSONAL_DOCUMENTS_PART_URL,
                                component: EmployeeCardPersonalDocumentsComponent
                            },
                            {
                                path: RouterClientConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_MILITARY_REGISTRATION_PART_URL,
                                component: EmployeeCardMilitaryRegistrationComponent
                            }
                        ]
                    }
                ]
            },
        ]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class OrgStructuresRouterModule {

}

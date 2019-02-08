import {RouterModule, Routes} from '@angular/router';
import {NgModule} from '@angular/core';
import {OrgStructuresComponent} from './org-structures.component';
import {EmployeesListComponent} from './employees/list/employees-list.component';
import {DepartmentsComponent} from './departments/departments.component';
import {PostsComponent} from './posts/posts.component';
import {OrgStructuresSyncComponent} from './synch/org-structures-sync.component';
import {TypeWorkComponent} from './type-work/type-work.component';
import {EmployeeCardComponent} from './employees/employee-card/employee-card.component';
import {EmployeesComponent} from './employees/employees.component';
import {EmployeeCardCommonsComponent} from './employees/employee-card/commons/employee-card-commons.component';
import {EmployeeCardEducationsComponent} from './employees/employee-card/educations/employee-card-educations.component';
import {RouterConstant} from "../../../@core/constants/router.constant";
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


const routes: Routes = [
    {path: '', component: OrgStructuresComponent, redirectTo: 'employees'},
    {path: 'departments', component: DepartmentsComponent},
    {path: 'posts', component: PostsComponent},
    {path: 'sync', component: OrgStructuresSyncComponent},
    {path: 'type-work', component: TypeWorkComponent},
    {
        path: RouterConstant.ORG_STRUCTURES_EMPLOYEES_PART_URL, component: EmployeesComponent,
        children: [
            {path: '', redirectTo: 'list'},
            {path: 'list', component: EmployeesListComponent},
            {
                path: RouterConstant.ORG_STRUCTURES_EMPLOYEES_ID_PART_URL, component: EmptyRouterComponent,
                children: [
                    {path: '', redirectTo: RouterConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_PART_URL},
                    {
                        path: RouterConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_PART_URL,
                        component: EmployeeCardComponent,
                        children: [
                            {path: '', redirectTo: RouterConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_COMMONS_PART_URL},
                            {
                                path: RouterConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_COMMONS_PART_URL,
                                component: EmployeeCardCommonsComponent
                            },
                            {
                                path: RouterConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_EDUCATIONS_PART_URL,
                                component: EmployeeCardEducationsComponent
                            },
                            {
                                path: RouterConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_ACHIEVEMENTS_PART_URL,
                                component: EmployeeCardAchievementsComponent
                            },
                            {
                                path: RouterConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_PUNISHMENTS_PART_URL,
                                component: EmployeeCardPunishmentsComponent
                            },
                            {
                                path: RouterConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_EMPLOYMENT_HISTORIES_PART_URL,
                                component: EmployeeCardEmploymentHistoriesComponent
                            },
                            {
                                path: RouterConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_STAFF_MOVEMENTS_PART_URL,
                                component: EmployeeCardStaffMovementsComponent
                            },
                            {
                                path: RouterConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_COMBINATIONS_PART_URL,
                                component: EmployeeCardCombinationsComponent
                            },
                            {
                                path: RouterConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_VACATIONS_PART_URL,
                                component: EmployeeCardVacationsComponent
                            },
                            {
                                path: RouterConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_PERSONAL_DATA_PART_URL,
                                component: EmployeeCardPersonalDataComponent
                            },
                            {
                                path: RouterConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_RELATIVES_PART_URL,
                                component: EmployeeCardRelativesComponent
                            },
                            {
                                path: RouterConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_PERSONAL_DOCUMENTS_PART_URL,
                                component: EmployeeCardPersonalDocumentsComponent
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

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
import {EmployeeCardEmploymentHistoriesComponent} from "./employees/employee-card/employment-histories/employee-card-employment-histories.component";


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
                        path: RouterConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_PART_URL, component: EmployeeCardComponent,
                        children: [
                            {path: '', redirectTo: RouterConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_COMMONS_PART_URL},
                            {path: RouterConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_COMMONS_PART_URL,
                                component: EmployeeCardCommonsComponent},
                            {path: RouterConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_EDUCATIONS_PART_URL,
                                component: EmployeeCardEducationsComponent},
                            {path: RouterConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_ACHIEVEMENTS_PART_URL,
                                component: EmployeeCardAchievementsComponent},
                            {path: RouterConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_PUNISHMENTS_PART_URL,
                                component: EmployeeCardPunishmentsComponent},
                            {path: RouterConstant.ORG_STRUCTURES_EMPLOYEES_ID_DETAILS_EMPLOYMENT_HISTORIES_PART_URL,
                                component: EmployeeCardEmploymentHistoriesComponent}
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

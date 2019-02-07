import {NgModule} from '@angular/core';
import {EmployeesListComponent} from './employees/list/employees-list.component';
import {OrgStructuresComponent} from './org-structures.component';
import {OrgStructuresRouterModule} from './org-structures-router.module';
import {ThemeModule} from '../../../@theme/theme.module';
import {DepartmentsComponent} from './departments/departments.component';
import {PostsComponent} from './posts/posts.component';
import {OrgStructuresSyncComponent} from './synch/org-structures-sync.component';
import {TypeWorkComponent} from './type-work/type-work.component';
import {EmployeeCardComponent} from './employees/employee-card/employee-card.component';
import {EmployeesComponent} from './employees/employees.component';
import {EmployeeCardCommonsComponent} from './employees/employee-card/commons/employee-card-commons.component';
import {EmployeeCardEducationsComponent} from './employees/employee-card/educations/employee-card-educations.component';
import {EmployeeCardAchievementsComponent} from "./employees/employee-card/achievements/employee-card-achievements.component";
import {EmployeeCardPunishmentsComponent} from "./employees/employee-card/punishments/employee-card-punishments.component";
import {EmployeeCardEmploymentHistoriesComponent} from "./employees/employee-card/employment-histories/employee-card-employment-histories.component";

@NgModule({
    imports: [
        ThemeModule,
        OrgStructuresRouterModule
    ],
    declarations: [
        OrgStructuresComponent,
        DepartmentsComponent,
        PostsComponent,
        EmployeesComponent,
        EmployeesListComponent,
        OrgStructuresSyncComponent,
        TypeWorkComponent,
        EmployeeCardComponent,
        EmployeeCardCommonsComponent,
        EmployeeCardEducationsComponent,
        EmployeeCardAchievementsComponent,
        EmployeeCardPunishmentsComponent,
        EmployeeCardEmploymentHistoriesComponent
    ]
})
export class OrgStructuresModule {

}

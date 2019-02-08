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
import {EmployeeCardEmploymentHistoriesComponent} from "./employees/employee-card/employment-histories/employee-card-employment-histories.component"; // tslint:disable
import {EmployeeCardStaffMovementsComponent} from "./employees/employee-card/staff-movements/employee-card-staff-movements.component";
import {EmployeeCardCombinationsComponent} from "./employees/employee-card/combinations/employee-card-combinations.component";
import {EmployeeCardVacationsComponent} from "./employees/employee-card/vacations/employee-card-vacations.component";
import {EmployeeCardPersonalDataComponent} from "./employees/employee-card/personal-data/employee-card-personal-data.component";

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
        EmployeeCardEmploymentHistoriesComponent,
        EmployeeCardStaffMovementsComponent,
        EmployeeCardCombinationsComponent,
        EmployeeCardVacationsComponent,
        EmployeeCardPersonalDataComponent
    ]
})
export class OrgStructuresModule {

}

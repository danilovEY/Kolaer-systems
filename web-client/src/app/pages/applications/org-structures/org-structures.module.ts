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
import {EmployeeCardRelativesComponent} from "./employees/employee-card/relatives/employee-card-relatives.component";
import {EmployeeCardPersonalDocumentsComponent} from "./employees/employee-card/personal-documents/employee-card-personal-documents.component";
import {EmployeeCardMilitaryRegistrationComponent} from "./employees/employee-card/military-registration/employee-card-military-registration.component";
import {EmployeeCardService} from "./employees/employee-card/employee-card.service";
import {EmployeeListGuardService} from "./services/employee-list-guard.service";
import {EmployeeEducationsGuardService} from "./services/employee-educations-guard.service";
import {EmployeeEducationService} from "./employees/employee-card/educations/employee-education.service";
import {EmployeeMilitaryRegistrationService} from "./employees/employee-card/military-registration/employee-military-registration.service";
import {EmployeeAchievementService} from "./employees/employee-card/achievements/employee-achievement.service";
import {EmployeeCombinationsService} from "./employees/employee-card/combinations/employee-combinations.service";
import {EmployeeEmploymentHistoriesService} from "./employees/employee-card/employment-histories/employee-employment-histories.service";
import {EmployeePersonalDataService} from "./employees/employee-card/personal-data/employee-personal-data.service";
import {EmployeePersonalDocumentService} from "./employees/employee-card/personal-documents/employee-personal-document.service";
import {EmployeePunishmentService} from "./employees/employee-card/punishments/employee-punishment.service";
import {EmployeeRelativeService} from "./employees/employee-card/relatives/employee-relative.service";
import {EmployeeStaffMovementsService} from "./employees/employee-card/staff-movements/employee-staff-movements.service";

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
        EmployeeCardPersonalDataComponent,
        EmployeeCardRelativesComponent,
        EmployeeCardPersonalDocumentsComponent,
        EmployeeCardMilitaryRegistrationComponent
    ],
    providers: [
        EmployeeCardService,
        EmployeeListGuardService,
        EmployeeEducationsGuardService,
        EmployeeEducationService,
        EmployeeMilitaryRegistrationService,
        EmployeeAchievementService,
        EmployeeCombinationsService,
        EmployeeEmploymentHistoriesService,
        EmployeePersonalDataService,
        EmployeePersonalDocumentService,
        EmployeePunishmentService,
        EmployeeRelativeService,
        EmployeeStaffMovementsService
    ]
})
export class OrgStructuresModule {

}

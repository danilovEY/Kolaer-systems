import {NgModule} from '@angular/core';
import {VacationRouterModule} from './vacation-router.module';
import {VacationComponent} from './vacation.component';
import {VacationMainComponent} from './main/vacation-main.component';
import {ThemeModule} from '../../../@theme/theme.module';
import {VacationSetComponent} from './set/vacation-set.component';
import {VacationService} from './vacation.service';
import {VacationDateFromEditComponent} from './set/vacation-date-from-edit.component';
import {VacationDateToEditComponent} from './set/vacation-date-to-edit.component';
import {VacationDaysEditComponent} from './set/vacation-days-edit.component';
import {VacationReportComponent} from './report/vacation-report.component';

@NgModule({
    imports: [
        VacationRouterModule,
        ThemeModule
    ],
    declarations: [
        VacationComponent,
        VacationMainComponent,
        VacationSetComponent,
        VacationDateFromEditComponent,
        VacationDateToEditComponent,
        VacationDaysEditComponent,
        VacationReportComponent
    ],
    entryComponents: [
        VacationDateFromEditComponent,
        VacationDateToEditComponent,
        VacationDaysEditComponent,
    ],
    providers: [
        VacationService
    ]
})
export class VacationModule {

}

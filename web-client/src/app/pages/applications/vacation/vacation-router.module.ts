import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {VacationComponent} from './vacation.component';
import {VacationMainComponent} from './main/vacation-main.component';
import {VacationSetComponent} from './set/vacation-set.component';
import {VacationReportCalendarComponent} from './report/calendar/vacation-report-calendar.component';
import {VacationGuardService} from '../../../@core/modules/auth/vacation-guard.service';
import {VacationReportDistributeComponent} from './report/distribute/vacation-report-distribute.component';

const routers: Routes = [
    {
        path: '',
        component: VacationComponent,
        redirectTo: 'main'
    },
    {
        path: 'main',
        component: VacationMainComponent
    },
    {
        path: 'set',
        component: VacationSetComponent,
        canActivate: [VacationGuardService]
    },
    {
        path: 'report/calendar',
        component: VacationReportCalendarComponent,
        canActivate: [VacationGuardService]
    },
    {
        path: 'report/distribute',
        component: VacationReportDistributeComponent,
        canActivate: [VacationGuardService]
    }
];

@NgModule({
    imports: [
        RouterModule.forChild(routers)
    ],
    exports: [
        RouterModule
    ]
})
export class VacationRouterModule {

}

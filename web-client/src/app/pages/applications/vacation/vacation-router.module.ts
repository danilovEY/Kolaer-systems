import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {VacationComponent} from './vacation.component';
import {VacationMainComponent} from './main/vacation-main.component';
import {VacationSetComponent} from './set/vacation-set.component';
import {VacationReportCalendarComponent} from './report/calendar/vacation-report-calendar.component';
import {VacationGuardService} from './vacation-guard.service';
import {VacationReportDistributeComponent} from './report/distribute/vacation-report-distribute.component';
import {VacationExportComponent} from './report/export/vacation-export.component';
import {VacationReportTotalCountComponent} from './report/total-count/vacation-report-total-count.component';

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
    },
    {
        path: 'report/total-count',
        component: VacationReportTotalCountComponent,
        canActivate: [VacationGuardService]
    },
    {
        path: 'report/export',
        component: VacationExportComponent,
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

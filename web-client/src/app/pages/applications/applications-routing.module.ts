import {RouterModule, Routes} from '@angular/router';
import {NgModule} from '@angular/core';
import {ApplicationsComponent} from './applications.component';
import {AuthGuardService} from '../../@core/modules/auth/auth-guard.service';


const routes: Routes = [{
    path: '',
    component: ApplicationsComponent,
    children: [
        {
            path: 'kolpass',
            loadChildren: 'app/pages/applications/kolpass/kolpass.module#KolpassModule',
            canActivate: [AuthGuardService]
        },
        {
            path: 'tickets',
            loadChildren: 'app/pages/applications/tickets/tickets.module#TicketsModule'
        },
        {
            path: 'org-structures',
            loadChildren: 'app/pages/applications/org-structures/org-structures.module#OrgStructuresModule'
        },
        {
            path: 'production-calendar',
            loadChildren: 'app/pages/applications/production-calendar/production-calendar.module#ProductionCalendarModule'
        },
    ]
}];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule],
})
export class ApplicationsRoutingModule {
}

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
            loadChildren: 'app/pages/applications/tickets/tickets.module#TicketsModule',
            canActivate: [AuthGuardService]
        },
        {
            path: 'org-structures',
            loadChildren: 'app/pages/applications/org-structures/org-structures.module#OrgStructuresModule',
            canActivate: [AuthGuardService]
        },
        {
            path: 'production-calendar',
            loadChildren: 'app/pages/applications/production-calendar/production-calendar.module#ProductionCalendarModule'
        },
        {
            path: 'contacts',
            loadChildren: 'app/pages/applications/contacts/contacts.module#ContactsModule'
        },
        {
            path: 'queue',
            loadChildren: 'app/pages/applications/queue/queue.module#QueueModule',
            canActivate: [AuthGuardService]
        },
        {
            path: 'vacation',
            loadChildren: 'app/pages/applications/vacation/vacation.module#VacationModule',
            canActivate: [AuthGuardService]
        },
    ]
}];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule],
})
export class ApplicationsRoutingModule {
}

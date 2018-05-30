import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {TicketsComponent} from './tickets.component';
import {TicketsMainComponent} from './main/tickets-main.component';
import {BankAccountsComponent} from './bank-accounts/bank-accounts.component';
import {AdminGuardService} from '../../../@core/modules/auth/admin-guard.service';

const routes: Routes = [
    {
        path: '',
        redirectTo: 'main',
        component: TicketsComponent
    },
    {
        path: 'main',
        component: TicketsMainComponent
    },
    {
        path: 'bank-accounts',
        component: BankAccountsComponent,
        canActivate: [AdminGuardService]
    }
];

@NgModule({
    imports: [
        RouterModule.forChild(routes)
    ],
    exports: [
        RouterModule
    ]
})
export class TicketsRouterModule {

}

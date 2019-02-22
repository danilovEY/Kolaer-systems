import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {TicketsComponent} from './tickets.component';
import {TicketsMainComponent} from './main/tickets-main.component';
import {BankAccountsComponent} from './bank-accounts/bank-accounts.component';
import {RegisterDetailedComponent} from './register-detailed/register-detailed.component';
import {BankAccountGuardService} from "./bank-account-guard.service";

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
        path: 'register/:id',
        component: RegisterDetailedComponent
    },
    {
        path: 'bank-accounts',
        component: BankAccountsComponent,
        canActivate: [BankAccountGuardService]
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

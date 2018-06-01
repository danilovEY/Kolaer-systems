import {NgModule} from '@angular/core';
import {TicketsRouterModule} from './tickets-routing.module';
import {TicketsMainComponent} from './main/tickets-main.component';
import {TicketsComponent} from './tickets.component';
import {ThemeModule} from '../../../@theme/theme.module';
import {TicketsService} from './tickets.service';
import {BankAccountsComponent} from './bank-accounts/bank-accounts.component';
import {BankAccountService} from './bank-accounts/bank-account.service';
import {RegisterDetailedComponent} from './register-detailed/register-detailed.component';

@NgModule({
    imports: [
        TicketsRouterModule,
        ThemeModule
    ],
    declarations: [
        TicketsMainComponent,
        TicketsComponent,
        BankAccountsComponent,
        RegisterDetailedComponent
    ],
    providers: [
        TicketsService,
        BankAccountService,
    ]
})
export class TicketsModule {

}

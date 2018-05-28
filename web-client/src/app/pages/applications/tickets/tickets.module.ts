import {NgModule} from '@angular/core';
import {TicketsRouterModule} from './tickets-routing.module';
import {TicketsMainComponent} from './main/tickets-main.component';
import {TicketsComponent} from './tickets.component';
import {ThemeModule} from '../../../@theme/theme.module';
import {TicketsService} from './tickets.service';

@NgModule({
    imports: [
        TicketsRouterModule,
        ThemeModule
    ],
    declarations: [
        TicketsMainComponent,
        TicketsComponent
    ],
    providers: [
        TicketsService
    ]
})
export class TicketsModule {

}

import {NgModule} from "@angular/core";
import {TicketsRouterModule} from "./tickets-routing.module";
import {TicketsMainComponent} from "./main/tickets-main.component";

@NgModule({
    imports: [
        TicketsRouterModule
    ],
    declarations: [
        TicketsMainComponent
    ]
})
export class TicketsModule {

}

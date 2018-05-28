import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {TicketsComponent} from './tickets.component';
import {TicketsMainComponent} from "./main/tickets-main.component";

const routes: Routes = [
    {
        path: '',
        redirectTo: 'main',
        component: TicketsComponent
    },
    {
        path: 'main',
        component: TicketsMainComponent
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

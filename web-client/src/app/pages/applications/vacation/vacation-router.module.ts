import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {VacationComponent} from './vacation.component';
import {VacationMainComponent} from './main/vacation-main.component';
import {VacationSetComponent} from './set/vacation-set.component';

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
        component: VacationSetComponent
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

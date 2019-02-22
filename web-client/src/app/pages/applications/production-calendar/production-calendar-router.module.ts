import {NgModule} from '@angular/core';
import {Route, RouterModule} from '@angular/router';
import {ProductionCalendarComponent} from './production-calendar.component';
import {ProductionCalendarMainComponent} from './main/production-calendar-main.component';
import {ProductionCalendarEditComponent} from './edit/production-calendar-edit.component';

const router: Route[] = [
    {
        path: '',
        redirectTo: 'main',
        component: ProductionCalendarComponent
    },
    {
        path: 'main',
        component: ProductionCalendarMainComponent
    },
    {
        path: 'edit',
        component: ProductionCalendarEditComponent
    }
];

@NgModule({
    imports: [RouterModule.forChild(router)],
    exports: [RouterModule]
})
export class ProductionCalendarRouterModule {

}

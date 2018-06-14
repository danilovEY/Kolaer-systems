import {NgModule} from '@angular/core';
import {ProductionCalendarComponent} from './production-calendar.component';
import {ProductionCalendarRouterModule} from './production-calendar-router.module';
import {ThemeModule} from '../../../@theme/theme.module';
import {ProductionCalendarMainComponent} from './main/production-calendar-main.component';
import {ProductionCalendarEditComponent} from './edit/production-calendar-edit.component';

@NgModule({
    declarations: [
        ProductionCalendarComponent,
        ProductionCalendarMainComponent,
        ProductionCalendarEditComponent,
    ],
    imports: [
        ThemeModule,
        ProductionCalendarRouterModule
    ]
})
export class ProductionCalendarModule {

}

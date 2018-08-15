import {NgModule} from '@angular/core';
import {VacationRouterModule} from './vacation-router.module';
import {VacationComponent} from './vacation.component';
import {VacationMainComponent} from './main/vacation-main.component';
import {ThemeModule} from '../../../@theme/theme.module';
import {VacationSetComponent} from './set/vacation-set.component';
import {VacationService} from './vacation.service';

@NgModule({
    imports: [
        VacationRouterModule,
        ThemeModule
    ],
    declarations: [
        VacationComponent,
        VacationMainComponent,
        VacationSetComponent
    ],
    providers: [
        VacationService
    ]
})
export class VacationModule {

}

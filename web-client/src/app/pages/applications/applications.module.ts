import {NgModule} from '@angular/core';
import {ApplicationsComponent} from './applications.component';
import {ApplicationsRoutingModule} from './applications-routing.module';
import {ThemeModule} from '../../@theme/theme.module';


@NgModule({
    imports: [
        ApplicationsRoutingModule,
        ThemeModule,
    ],
    declarations: [
        ApplicationsComponent
    ],
    exports: [
        ApplicationsComponent
    ]
})
export class ApplicationsModule {

}


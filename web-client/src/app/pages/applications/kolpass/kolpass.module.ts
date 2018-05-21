import {NgModule} from '@angular/core';
import {ThemeModule} from '../../../@theme/theme.module';
import {KolpassRouteModule} from './kolpass-route.module';
import {KolpassComponent} from './kolpass.component';
import {KolpassService} from './kolpass.service';


@NgModule({
    imports: [
        KolpassRouteModule,
        ThemeModule,
    ],
    providers: [
        KolpassService
    ],
    declarations: [
        KolpassComponent,
    ],
    exports: [
        KolpassComponent
    ]
})
export class KolpassModule {
    
}

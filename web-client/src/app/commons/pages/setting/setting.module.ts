import {NgModule} from '@angular/core';
import {SettingComponent} from './setting.component';
import {SettingRoutingModule} from './setting.routing';
import {AppCoreModule} from '../../app-core.module';

@NgModule({
    imports: [
        AppCoreModule,
        SettingRoutingModule
    ],
    declarations: [
        SettingComponent
    ]
})
export class SettingModule {
    constructor() {

    }
}

import {NgModule} from '@angular/core';
import {SettingComponent} from './setting.component';
import {SettingRoutingModule} from './setting.routing';
import {AppCoreModule} from '../../app-core.module';
import {ActiveModule} from 'angular-bootstrap-md/index';

@NgModule({
    imports: [
        AppCoreModule,
        ActiveModule.forRoot(),
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

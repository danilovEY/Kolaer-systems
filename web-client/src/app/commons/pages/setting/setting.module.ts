import {NgModule} from '@angular/core';
import {SettingComponent} from './setting.component';
import {SettingRoutingModule} from './setting.routing';
import {AppCoreModule} from '../../app-core.module';
import {ActiveModule, ModalModule} from 'angular-bootstrap-md/index';
import {DeepModule} from 'angular-bootstrap-md/inputs/deep.module';
import {ReactiveFormsModule} from '@angular/forms';

@NgModule({
    imports: [
        AppCoreModule,
        ReactiveFormsModule,
        ModalModule.forRoot(),
        ActiveModule.forRoot(),
        DeepModule.forRoot(),
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

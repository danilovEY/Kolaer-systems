import {NgModule} from '@angular/core';
import {NavbarModule} from '../../modules/navbar/navbar.module';
import {SettingComponent} from './setting.component';
import {SettingRoutingModule} from './setting.routing';
import {BrowserModule} from '@angular/platform-browser';

@NgModule({
    imports: [
        BrowserModule,
        NavbarModule,
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

import {ModuleWithProviders, NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {NgbDatepickerI18n, NgbModule} from '@ng-bootstrap/ng-bootstrap';

import {
    NbActionsModule,
    NbCardModule,
    NbCheckboxModule,
    NbContextMenuModule,
    NbLayoutModule,
    NbMenuModule,
    NbPopoverModule,
    NbRouteTabsetModule,
    NbSearchModule,
    NbSidebarModule,
    NbTabsetModule,
    NbThemeModule,
    NbUserModule,
} from '@nebular/theme';

import {NbSecurityModule} from '@nebular/security';

import {
    CustomActionEditComponent,
    CustomActionViewComponent,
    CustomLoginComponent,
    CustomLogoutComponent,
    CustomTableComponent,
    FooterComponent,
    HeaderComponent,
    NotFoundComponent,
    SearchInputComponent
} from './components';
import {CapitalizePipe, PluralPipe, RoundPipe, TimingPipe} from './pipes';
import {DefaultLayoutComponent} from './layouts';
import {DEFAULT_THEME} from './styles/theme.default';
import {COSMIC_THEME} from './styles/theme.cosmic';
import {LoadingModule} from 'ngx-loading';
import {Ng2SmartTableModule} from 'ng2-smart-table';
import {ToasterModule} from 'angular2-toaster';
import {EmployeeEditComponent} from './components/table/employee-edit.component';
import {NgSelectModule} from '@ng-select/ng-select';
import {AccountEditComponent} from './components/table/account-edit.component';
import {PostEditComponent} from './components/table/post-edit.component';
import {DepartmentEditComponent} from './components/table/department-edit.component';
import {DateEditComponent} from './components/table/date-edit.component';
import {CustomDatePickerLangService} from "../@core/services/custom-date-picker-lang.service";

const BASE_MODULES = [
    CommonModule,
    FormsModule,
    ReactiveFormsModule
];

const NB_MODULES = [
    NbCardModule,
    NbLayoutModule,
    NbTabsetModule,
    NbRouteTabsetModule,
    NbMenuModule,
    NbUserModule,
    NbActionsModule,
    NbSearchModule,
    NbSidebarModule,
    NbCheckboxModule,
    NbPopoverModule,
    NbContextMenuModule,
    NgbModule,
    NbSecurityModule, // *nbIsGranted directive

    LoadingModule,
    Ng2SmartTableModule,
    ToasterModule,
    NgSelectModule,
];

const COMPONENTS = [
    HeaderComponent,
    FooterComponent,
    SearchInputComponent,
    DefaultLayoutComponent,
    CustomLoginComponent,
    CustomLogoutComponent,
    NotFoundComponent,
    CustomActionViewComponent,
    CustomActionEditComponent,
    CustomTableComponent,
    EmployeeEditComponent,
    AccountEditComponent,
    PostEditComponent,
    DepartmentEditComponent,
    DateEditComponent,

];

const PIPES = [
    CapitalizePipe,
    PluralPipe,
    RoundPipe,
    TimingPipe,
];

const NB_THEME_PROVIDERS = [
    ...NbThemeModule.forRoot(
        {
            name: 'default',
        },
        [DEFAULT_THEME, COSMIC_THEME],
    ).providers,
    ...NbSidebarModule.forRoot().providers,
    ...NbMenuModule.forRoot().providers,
    ...ToasterModule.forRoot().providers,
    {provide: NgbDatepickerI18n, useClass: CustomDatePickerLangService}

];

@NgModule({
    imports: [...BASE_MODULES, ...NB_MODULES],
    exports: [...BASE_MODULES, ...NB_MODULES, ...COMPONENTS, ...PIPES],
    declarations: [...COMPONENTS, ...PIPES],
    entryComponents: [
        CustomActionViewComponent,
        CustomActionEditComponent,
        CustomTableComponent,
        EmployeeEditComponent,
        AccountEditComponent,
        PostEditComponent,
        DepartmentEditComponent,
        DateEditComponent,
    ]
})
export class ThemeModule {
    static forRoot(): ModuleWithProviders {
        return <ModuleWithProviders>{
            ngModule: ThemeModule,
            providers: [...NB_THEME_PROVIDERS],
        };
    }
}

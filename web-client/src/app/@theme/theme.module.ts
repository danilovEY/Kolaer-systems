import {ModuleWithProviders, NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {NgbDateParserFormatter, NgbDatepickerI18n, NgbModule} from '@ng-bootstrap/ng-bootstrap';

import {
    NbActionsModule,
    NbAlertModule,
    NbCardModule,
    NbCheckboxModule,
    NbContextMenuModule,
    NbLayoutModule,
    NbMenuModule,
    NbPopoverModule,
    NbProgressBarModule,
    NbRouteTabsetModule,
    NbSearchModule,
    NbSidebarModule,
    NbTabsetModule,
    NbThemeModule,
    NbUserModule
} from '@nebular/theme';
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
import {CustomDatePickerLangService} from '../@core/services/custom-date-picker-lang.service';
import {PlacementEditComponent} from './components/table/placement-edit.component';
import {AutoCompleteModule, CalendarModule, DropdownModule, MessagesModule} from 'primeng/primeng';
import {DateTimeEditComponent} from './components/table/date-time-edit.component';
import {NgbDateRusParserFormatter} from './components/table/ngb-date-rus-parser.formatter';
import {TableModule} from 'primeng/table';
import {MessageModule} from 'primeng/message';
import {TypeWorkEditComponent} from './components/table/type-work-edit.component';

// import {NbSecurityModule} from '@nebular/security';

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
    NbProgressBarModule,
    NbAlertModule,
    NgbModule,
    // NbSecurityModule, // *nbIsGranted directive

    LoadingModule,
    Ng2SmartTableModule,
    ToasterModule,
    NgSelectModule,
    CalendarModule,
    TableModule,
    DropdownModule,
    MessagesModule,
    MessageModule,
    AutoCompleteModule
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
    DateTimeEditComponent,
    PlacementEditComponent,
    TypeWorkEditComponent,
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
    {provide: NgbDatepickerI18n, useClass: CustomDatePickerLangService},
    {provide: NgbDateParserFormatter, useClass: NgbDateRusParserFormatter},

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
        DateTimeEditComponent,
        PlacementEditComponent,
        TypeWorkEditComponent,
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

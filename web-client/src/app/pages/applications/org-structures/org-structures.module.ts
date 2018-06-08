import {NgModule} from '@angular/core';
import {EmployeesComponent} from './employees/employees.component';
import {OrgStructuresComponent} from './org-structures.component';
import {OrgStructuresRouterModule} from "./org-structures-router.module";
import {ThemeModule} from "../../../@theme/theme.module";

@NgModule({
    imports: [
        ThemeModule,
        OrgStructuresRouterModule
    ],
    declarations: [
        OrgStructuresComponent,
        EmployeesComponent
    ]
})
export class OrgStructuresModule {

}

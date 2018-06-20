import {NgModule} from '@angular/core';
import {EmployeesComponent} from './employees/employees.component';
import {OrgStructuresComponent} from './org-structures.component';
import {OrgStructuresRouterModule} from './org-structures-router.module';
import {ThemeModule} from '../../../@theme/theme.module';
import {DepartmentsComponent} from './departments/departments.component';
import {PostsComponent} from './posts/posts.component';
import {OrgStructuresSyncComponent} from './synch/org-structures-sync.component';

@NgModule({
    imports: [
        ThemeModule,
        OrgStructuresRouterModule
    ],
    declarations: [
        OrgStructuresComponent,
        DepartmentsComponent,
        PostsComponent,
        EmployeesComponent,
        OrgStructuresSyncComponent,
    ]
})
export class OrgStructuresModule {

}

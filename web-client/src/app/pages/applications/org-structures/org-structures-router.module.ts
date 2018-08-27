import {RouterModule, Routes} from '@angular/router';
import {NgModule} from '@angular/core';
import {OrgStructuresComponent} from './org-structures.component';
import {EmployeesComponent} from './employees/employees.component';
import {DepartmentsComponent} from './departments/departments.component';
import {PostsComponent} from './posts/posts.component';
import {OrgStructuresSyncComponent} from './synch/org-structures-sync.component';
import {TypeWorkComponent} from './type-work/type-work.component';

const routes: Routes = [
    {
        path: '',
        component: OrgStructuresComponent,
        redirectTo: 'employees'
    },
    {
        path: 'employees',
        component: EmployeesComponent
    },
    {
        path: 'departments',
        component: DepartmentsComponent
    },
    {
        path: 'posts',
        component: PostsComponent
    },
    {
        path: 'sync',
        component: OrgStructuresSyncComponent
    },
    {
        path: 'type-work',
        component: TypeWorkComponent
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class OrgStructuresRouterModule {

}

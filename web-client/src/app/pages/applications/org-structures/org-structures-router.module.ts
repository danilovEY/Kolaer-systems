import {RouterModule, Routes} from '@angular/router';
import {NgModule} from '@angular/core';
import {OrgStructuresComponent} from './org-structures.component';
import {EmployeesComponent} from './employees/employees.component';
import {DepartmentsComponent} from './departments/departments.component';
import {PostsComponent} from './posts/posts.component';

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
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class OrgStructuresRouterModule {

}

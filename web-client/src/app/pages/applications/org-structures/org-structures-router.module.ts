import {RouterModule, Routes} from '@angular/router';
import {NgModule} from '@angular/core';
import {OrgStructuresComponent} from './org-structures.component';
import {EmployeesComponent} from './employees/employees.component';

const routes: Routes = [
    {
        path: '',
        component: OrgStructuresComponent,
        redirectTo: 'employees'
    },
    {
        path: 'employees',
        component: EmployeesComponent
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class OrgStructuresRouterModule {

}

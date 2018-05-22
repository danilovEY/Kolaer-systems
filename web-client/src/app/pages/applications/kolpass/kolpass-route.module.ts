import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {RepositoryDetailedComponent} from './detailed/repository-detailed.component';
import {RepositoriesComponent} from './repositories/repositories.component';
import {KolpassComponent} from './kolpass.component';

const routes: Routes = [
    {
        path: '',
        redirectTo: 'repositories',
        component: KolpassComponent,
    },
    {
        path: 'repositories',
        component: RepositoriesComponent
    },
    {
        path: 'repository/:id',
        component: RepositoryDetailedComponent
    }
];


@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule],
})
export class KolpassRouteModule {

}

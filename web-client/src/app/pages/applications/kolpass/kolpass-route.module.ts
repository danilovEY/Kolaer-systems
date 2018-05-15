import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {KolpassComponent} from './kolpass.component';

const routes: Routes = [{
    path: '',
    component: KolpassComponent,
}];


@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule],
})
export class KolpassRouteModule {

}

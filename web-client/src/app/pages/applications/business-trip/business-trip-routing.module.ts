import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {BusinessTripListComponent} from "./list/business-trip-list.component";

const routes: Routes = [
    {
        path: '',
        redirectTo: 'list'
    },
    {
        path: 'list',
        component: BusinessTripListComponent
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule],
})
export class BusinessTripRoutingModule {

}

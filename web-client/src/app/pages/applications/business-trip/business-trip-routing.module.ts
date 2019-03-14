import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {BusinessTripListComponent} from "./list/business-trip-list.component";
import {BusinessTripListGuardService} from "./service/business-trip-list-guard.service";

const routes: Routes = [
    {
        path: '',
        redirectTo: 'list'
    },
    {
        path: 'list',
        component: BusinessTripListComponent,
        canActivate: [BusinessTripListGuardService]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule],
})
export class BusinessTripRoutingModule {

}

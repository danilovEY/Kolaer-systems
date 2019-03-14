import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {BusinessTripListComponent} from "./list/business-trip-list.component";
import {BusinessTripReadGuardService} from "./service/business-trip-read-guard.service";
import {BusinessTripDetailsComponent} from "./details/business-trip-details.component";
import {RouterClientConstant} from "../../../@core/constants/router-client.constant";
import {BusinessTripWriteGuardService} from "./service/business-trip-write-guard.service";

const routes: Routes = [
    {
        path: '',
        redirectTo: RouterClientConstant.BUSINESS_TRIP_LIST_PART_URL
    },
    {
        path: RouterClientConstant.BUSINESS_TRIP_LIST_PART_URL,
        component: BusinessTripListComponent,
        canActivate: [BusinessTripReadGuardService]
    },
    {
        path: RouterClientConstant.BUSINESS_TRIP_ID_PART_URL,
        component: BusinessTripDetailsComponent,
        canActivate: [BusinessTripWriteGuardService]
    },
    {
        path: RouterClientConstant.BUSINESS_TRIP_CREATE_PART_URL,
        component: BusinessTripDetailsComponent,
        canActivate: [BusinessTripWriteGuardService]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule],
})
export class BusinessTripRoutingModule {

}

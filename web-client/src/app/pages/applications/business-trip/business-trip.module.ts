import {NgModule} from "@angular/core";
import {BusinessTripComponent} from "./business-trip.component";
import {BusinessTripRoutingModule} from "./business-trip-routing.module";
import {BusinessTripListComponent} from "./list/business-trip-list.component";
import {BusinessTripService} from "./service/business-trip.service";
import {ThemeModule} from "../../../@theme/theme.module";
import {BusinessTripListGuardService} from "./service/business-trip-list-guard.service";

@NgModule({
    imports: [
        ThemeModule,
        BusinessTripRoutingModule
    ],
    declarations: [
        BusinessTripComponent,
        BusinessTripListComponent
    ],
    providers: [
        BusinessTripService,
        BusinessTripListGuardService
    ]
})
export class BusinessTripModule {

}

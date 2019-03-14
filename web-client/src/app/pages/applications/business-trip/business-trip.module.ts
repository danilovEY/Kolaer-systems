import {NgModule} from "@angular/core";
import {BusinessTripComponent} from "./business-trip.component";
import {BusinessTripRoutingModule} from "./business-trip-routing.module";
import {BusinessTripListComponent} from "./list/business-trip-list.component";
import {BusinessTripService} from "./service/business-trip.service";
import {ThemeModule} from "../../../@theme/theme.module";
import {BusinessTripReadGuardService} from "./service/business-trip-read-guard.service";
import {BusinessTripDetailsComponent} from "./details/business-trip-details.component";
import {BusinessTripWriteGuardService} from "./service/business-trip-write-guard.service";

@NgModule({
    imports: [
        ThemeModule,
        BusinessTripRoutingModule
    ],
    declarations: [
        BusinessTripComponent,
        BusinessTripListComponent,
        BusinessTripDetailsComponent
    ],
    providers: [
        BusinessTripService,
        BusinessTripReadGuardService,
        BusinessTripWriteGuardService
    ]
})
export class BusinessTripModule {

}

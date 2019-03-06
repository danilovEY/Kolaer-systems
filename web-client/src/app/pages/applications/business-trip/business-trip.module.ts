import {NgModule} from "@angular/core";
import {BusinessTripComponent} from "./business-trip.component";
import {BusinessTripRoutingModule} from "./business-trip-routing.module";
import {BusinessTripListComponent} from "./list/business-trip-list.component";
import {BusinessTripService} from "./business-trip.service";

@NgModule({
    imports: [
        BusinessTripRoutingModule
    ],
    declarations: [
        BusinessTripComponent,
        BusinessTripListComponent
    ],
    providers: [
        BusinessTripService
    ]
})
export class BusinessTripModule {

}

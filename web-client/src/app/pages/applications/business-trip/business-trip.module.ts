import {NgModule} from "@angular/core";
import {BusinessTripComponent} from "./business-trip.component";
import {BusinessTripRoutingModule} from "./business-trip-routing.module";
import {BusinessTripListComponent} from "./list/business-trip-list.component";
import {BusinessTripService} from "./service/business-trip.service";
import {ThemeModule} from "../../../@theme/theme.module";
import {BusinessTripReadGuardService} from "./service/business-trip-read-guard.service";
import {BusinessTripDetailsComponent} from "./details/business-trip-details.component";
import {BusinessTripWriteGuardService} from "./service/business-trip-write-guard.service";
import {BusinessTripDetailsEmployeeToEditComponent} from "./details/business-trip-details-employee-to-edit.component";
import {BusinessTripDetailsEmployeeFromEditComponent} from "./details/business-trip-details-employee-from-edit.component";
import {BusinessTripDetailsEmployeeDaysEditComponent} from "./details/business-trip-details-employee-days-edit.component";
import {MultiSelectModule} from "primeng/primeng";

@NgModule({
    imports: [
        ThemeModule,
        BusinessTripRoutingModule,
        MultiSelectModule
    ],
    declarations: [
        BusinessTripComponent,
        BusinessTripListComponent,
        BusinessTripDetailsComponent,
        BusinessTripDetailsEmployeeDaysEditComponent,
        BusinessTripDetailsEmployeeToEditComponent,
        BusinessTripDetailsEmployeeFromEditComponent,

    ],
    providers: [
        BusinessTripService,
        BusinessTripReadGuardService,
        BusinessTripWriteGuardService
    ],
    entryComponents: [
        BusinessTripDetailsEmployeeDaysEditComponent,
        BusinessTripDetailsEmployeeToEditComponent,
        BusinessTripDetailsEmployeeFromEditComponent
    ]
})
export class BusinessTripModule {

}

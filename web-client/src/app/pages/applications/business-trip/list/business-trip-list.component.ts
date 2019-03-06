import {Component} from "@angular/core";
import {BusinessTripService} from "../business-trip.service";

@Component({
    selector: 'business-trip-list',
    templateUrl: '/business-trip-list.component.html',
    styleUrls: ['/business-trip-list.component.scss']
})
export class BusinessTripListComponent {

    constructor(private businessTripService: BusinessTripService) {

    }

}

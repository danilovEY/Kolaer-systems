import {Component, OnDestroy, OnInit} from "@angular/core";
import {BusinessTripService} from "../service/business-trip.service";
import {ActivatedRoute} from "@angular/router";
import {Subject} from "rxjs";
import {filter, map, switchMap, takeUntil, tap} from "rxjs/operators";
import {PathVariableConstant} from "../../../../@core/constants/path-variable.constant";
import {BusinessTripDetailsModel} from "../model/business-trip-details.model";

@Component({
    selector: 'business-trip-details',
    templateUrl: './business-trip-details.component.html',
    styleUrls: ['./business-trip-details.component.scss']
})
export class BusinessTripDetailsComponent implements OnInit, OnDestroy {
    private readonly destroySubjects: Subject<any> = new Subject<any>();
    private businessTripId: number;

    currentBusinessTripDetailsModel: BusinessTripDetailsModel;

    constructor(private businessTripService: BusinessTripService,
                private activatedRoute: ActivatedRoute) {
        this.activatedRoute
            .params
            .pipe(
                filter(params => !isNaN(params[PathVariableConstant.BUSINESS_TRIP_ID])),
                map(params => params[PathVariableConstant.BUSINESS_TRIP_ID]),
                tap(param => this.businessTripId = param),
                switchMap((businessTripId: number) => this.businessTripService.getBusinessTripById(businessTripId)),
                takeUntil(this.destroySubjects)
            )
            .subscribe((businessTripDetails: BusinessTripDetailsModel) =>
                this.currentBusinessTripDetailsModel = businessTripDetails);
    }

    ngOnInit() {

    }

    ngOnDestroy() {
        this.destroySubjects.next(true);
        this.destroySubjects.complete();
    }

}

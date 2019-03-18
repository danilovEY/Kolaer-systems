import {Component, OnDestroy, OnInit} from "@angular/core";
import {BusinessTripService} from "../service/business-trip.service";
import {ActivatedRoute} from "@angular/router";
import {Subject} from "rxjs";
import {filter, map, switchMap, takeUntil, tap} from "rxjs/operators";
import {PathVariableConstant} from "../../../../@core/constants/path-variable.constant";
import {BusinessTripDetailsModel} from "../model/business-trip-details.model";
import {BusinessTripTypeEnum} from "../model/business-trip-type.enum";
import {BusinessTripTypeModel} from "../model/business-trip-type.model";
import {NgbDateStruct} from "@ng-bootstrap/ng-bootstrap";
import {AbstractControl, FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
    selector: 'business-trip-details',
    templateUrl: './business-trip-details.component.html',
    styleUrls: ['./business-trip-details.component.scss']
})
export class BusinessTripDetailsComponent implements OnInit, OnDestroy {
    private readonly destroySubjects: Subject<any> = new Subject<any>();
    private businessTripId: number;

    readonly businessTripTypes: BusinessTripTypeModel[] = [
        new BusinessTripTypeModel(BusinessTripTypeEnum.DIRECTION, 'Направление в командировку'),
        new BusinessTripTypeModel(BusinessTripTypeEnum.CHANGE, 'Изменение командировки'),
        new BusinessTripTypeModel(BusinessTripTypeEnum.CANCEL, 'Отмена командировки')
    ];

    currentBusinessTripDetailsModel: BusinessTripDetailsModel = new BusinessTripDetailsModel();
    isCreatePage: boolean = true;
    selectedBusinessTripType: BusinessTripTypeModel;
    currentDocumentDate: NgbDateStruct;
    currentReasonDocumentDate: NgbDateStruct;
    formBusinessTrip: FormGroup;

    constructor(private businessTripService: BusinessTripService,
                private activatedRoute: ActivatedRoute) {
    }

    ngOnInit(): void {
        this.activatedRoute
            .params
            .pipe(
                filter(params => !isNaN(params[PathVariableConstant.BUSINESS_TRIP_ID])),
                map(params => params[PathVariableConstant.BUSINESS_TRIP_ID]),
                tap(param => this.businessTripId = param),
                switchMap((businessTripId: number) => this.businessTripService.getBusinessTripById(businessTripId)),
                tap((businessTripDetails: BusinessTripDetailsModel) => this.currentBusinessTripDetailsModel = businessTripDetails),
                takeUntil(this.destroySubjects)
            )
            .subscribe((businessTripDetails: BusinessTripDetailsModel) => {
                this.isCreatePage = true;
                this.initBusinessTripFormValue(businessTripDetails);
            });

        this.formBusinessTrip = new FormGroup({
            documentNumber: new FormControl('', [Validators.minLength(3)]),
            documentDate: new FormControl('', [Validators.required]),
            businessTripType: new FormControl('', [Validators.required]),
            organizationName: new FormControl('', [Validators.minLength(3)]),
            okpoCode: new FormControl('', [Validators.minLength(3)]),
            comment: new FormControl('', [])
        });

        this.setBusinessTripType(BusinessTripTypeEnum.DIRECTION);
        this.setCurrentDocumentDate(new Date());
        this.setCurrentReasonDocumentDate(new Date());

        this.initBusinessTripFormValue(this.currentBusinessTripDetailsModel)
    }

    private initBusinessTripFormValue(businessTripDetails: BusinessTripDetailsModel) {
        if (this.formBusinessTrip && businessTripDetails) {
            this.setBusinessTripType(businessTripDetails.businessTripType);
            this.setCurrentDocumentDate(new Date(businessTripDetails.documentDate));

            this.formBusinessTrip.controls['documentNumber'].setValue(businessTripDetails.documentNumber);
            this.formBusinessTrip.controls['organizationName'].setValue(businessTripDetails.organizationName);
            this.formBusinessTrip.controls['okpoCode'].setValue(businessTripDetails.okpoCode);
            this.formBusinessTrip.controls['comment'].setValue(businessTripDetails.comment);
            this.formBusinessTrip.controls['documentDate'].setValue(this.currentDocumentDate);
            this.formBusinessTrip.controls['businessTripType'].setValue(this.selectedBusinessTripType);

            this.initReason(this.hasReason());
        }
    }

    private initReason(reason: boolean) {
        if (reason) {
            this.setCurrentReasonDocumentDate(new Date(this.currentBusinessTripDetailsModel.reasonDocumentDate));

            const reasonDescriptionFormControl: AbstractControl = this.formBusinessTrip.controls['reasonDescription']
                ? this.formBusinessTrip.controls['reasonDescription']
                : new FormControl('', [Validators.required, Validators.minLength(3)]);

            const reasonDocumentNumberFormControl: AbstractControl = this.formBusinessTrip.controls['reasonDocumentNumber']
                ? this.formBusinessTrip.controls['reasonDocumentNumber']
                : new FormControl('', [Validators.required, Validators.minLength(3)]);

            const reasonDocumentDateFormControl: AbstractControl = this.formBusinessTrip.controls['reasonDocumentDate']
                ? this.formBusinessTrip.controls['reasonDocumentDate']
                : new FormControl('', [Validators.required]);

            reasonDescriptionFormControl.setValue(this.currentBusinessTripDetailsModel.reasonDescription);
            reasonDocumentNumberFormControl.setValue(this.currentBusinessTripDetailsModel.reasonDocumentNumber);
            reasonDocumentDateFormControl.setValue(this.currentReasonDocumentDate);

            this.formBusinessTrip.setControl('reasonDescription', reasonDescriptionFormControl);
            this.formBusinessTrip.setControl('reasonDocumentNumber', reasonDocumentNumberFormControl);
            this.formBusinessTrip.setControl('reasonDocumentDate', reasonDocumentDateFormControl);
        } else {
            if (this.formBusinessTrip.controls['reasonDescription']) {
                this.formBusinessTrip.removeControl('reasonDescription');
            }

            if (this.formBusinessTrip.controls['reasonDocumentNumber']) {
                this.formBusinessTrip.removeControl('reasonDocumentNumber');
            }

            if (this.formBusinessTrip.controls['reasonDocumentDate']) {
                this.formBusinessTrip.removeControl('reasonDocumentDate');
            }
        }
    }

    private setCurrentDocumentDate(date: Date) {
        this.currentDocumentDate = {
            year: date.getFullYear(),
            month: date.getMonth() + 1,
            day: date.getDate()
        };
    }

    private setCurrentReasonDocumentDate(date: Date) {
        this.currentReasonDocumentDate = {
            year: date.getFullYear(),
            month: date.getMonth() + 1,
            day: date.getDate()
        };
    }

    private setBusinessTripType(businessTripType: BusinessTripTypeEnum) {
        switch (businessTripType) {
            case BusinessTripTypeEnum.CHANGE: {
                this.selectedBusinessTripType = this.businessTripTypes[1];
                break;
            }
            case BusinessTripTypeEnum.CANCEL: {
                this.selectedBusinessTripType = this.businessTripTypes[2];
                break;
            }
            default: {
                this.selectedBusinessTripType = this.businessTripTypes[0];
            }
        }
    }

    private selectBusinessTripType(selectBusinessTripType: BusinessTripTypeModel) {
        this.selectedBusinessTripType = selectBusinessTripType;

        this.initReason(this.hasReason());
    }

    ngOnDestroy() {
        this.destroySubjects.next(true);
        this.destroySubjects.complete();
    }

    private hasReason(): boolean {
        return this.selectedBusinessTripType &&
            this.selectedBusinessTripType.businessTripType !== BusinessTripTypeEnum.DIRECTION;
    }

    private submitBusinessTripForm() {
        const documentDateStructure: NgbDateStruct = this.formBusinessTrip.controls['documentDate'].value;

        this.currentBusinessTripDetailsModel.documentDate = new Date(
            documentDateStructure.year,
            documentDateStructure.month - 1,
            documentDateStructure.day
        );

        this.currentBusinessTripDetailsModel.documentNumber = this.formBusinessTrip.controls['documentNumber'].value;
        this.currentBusinessTripDetailsModel.organizationName = this.formBusinessTrip.controls['organizationName'].value;
        this.currentBusinessTripDetailsModel.okpoCode = this.formBusinessTrip.controls['okpoCode'].value;
        this.currentBusinessTripDetailsModel.comment = this.formBusinessTrip.controls['comment'].value;
        this.currentBusinessTripDetailsModel.businessTripType = this.formBusinessTrip.controls['businessTripType'].value.businessTripType;

        if (this.currentBusinessTripDetailsModel.businessTripType !== BusinessTripTypeEnum.DIRECTION) {
            const reasonDocumentDateStructure: NgbDateStruct = this.formBusinessTrip.controls['reasonDocumentDate'].value;

            this.currentBusinessTripDetailsModel.reasonDocumentDate = new Date(
                reasonDocumentDateStructure.year,
                reasonDocumentDateStructure.month - 1,
                reasonDocumentDateStructure.day
            );

            this.currentBusinessTripDetailsModel.reasonDescription = this.formBusinessTrip.controls['reasonDescription'].value;
            this.currentBusinessTripDetailsModel.reasonDocumentNumber = this.formBusinessTrip.controls['reasonDocumentNumber'].value;
        }

        console.log(this.currentBusinessTripDetailsModel);
    }
}

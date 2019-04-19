import {Component, OnDestroy, OnInit, ViewChild} from "@angular/core";
import {BusinessTripService} from "../service/business-trip.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Subject} from "rxjs";
import {filter, map, switchMap, takeUntil, tap} from "rxjs/operators";
import {PathVariableConstant} from "../../../../@core/constants/path-variable.constant";
import {BusinessTripDetailsModel} from "../model/business-trip-details.model";
import {BusinessTripTypeEnum} from "../model/business-trip-type.enum";
import {BusinessTripTypeModel} from "../model/business-trip-type.model";
import {NgbDateStruct} from "@ng-bootstrap/ng-bootstrap";
import {AbstractControl, FormControl, FormGroup, Validators} from "@angular/forms";
import {Column} from "ng2-smart-table/lib/data-set/column";
import {CustomTableComponent} from "../../../../@theme/components";
import {Cell} from "ng2-smart-table";
import {BusinessTripDetailsEmployeeDataSource} from "./business-trip-details-employee.data-source";
import {EmployeeEditComponent} from "../../../../@theme/components/table/employee-edit.component";
import {Utils} from "../../../../@core/utils/utils";
import {BusinessTripEmployeeModel} from "../model/business-trip-employee.model";
import {BusinessTripDetailsEmployeeFromEditComponent} from "./business-trip-details-employee-from-edit.component";
import {BusinessTripDetailsEmployeeToEditComponent} from "./business-trip-details-employee-to-edit.component";
import {BusinessTripDetailsEmployeeDaysEditComponent} from "./business-trip-details-employee-days-edit.component";
import {TableEventDeleteModel} from "../../../../@theme/components/table/table-event-delete.model";
import {TableEventAddModel} from "../../../../@theme/components/table/table-event-add.model";
import {TableEventEditModel} from "../../../../@theme/components/table/table-event-edit.model";
import {RouterClientConstant} from "../../../../@core/constants/router-client.constant";
import {EmployeeModel} from "../../../../@core/models/employee.model";
import {FindEmployeeRequestModel} from "../../../../@core/models/employee/request/find-employee-request.model";
import {EmployeeService} from "../../../../@core/services/employee.service";
import {BusinessTripCreateRequestModel} from "../model/business-trip-create-request.model";
import {BusinessTripEditRequestModel} from "../model/business-trip-edit-request.model";

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

    @ViewChild('businessTripTable')
    businessTripTable: CustomTableComponent;

    currentBusinessTripDetailsModel: BusinessTripDetailsModel = new BusinessTripDetailsModel();
    isCreatePage: boolean = true;
    selectedBusinessTripType: BusinessTripTypeModel;
    currentDocumentDate: NgbDateStruct;
    currentReasonDocumentDate: NgbDateStruct;
    formBusinessTrip: FormGroup;

    employeesLoading: boolean = true;
    employeeColumns: Column[] = [];
    employeesResult: EmployeeModel[] = [];
    employeeDataSource: BusinessTripDetailsEmployeeDataSource;

    constructor(private businessTripService: BusinessTripService, private activatedRoute: ActivatedRoute,
        private router: Router, private employeeService: EmployeeService) {}

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
                this.isCreatePage = false;
                this.initBusinessTripFormValue(businessTripDetails);
                this.initEmployeesTable(businessTripDetails);
            });

        this.formBusinessTrip = new FormGroup({
            documentNumber: new FormControl('', [Validators.minLength(3)]),
            documentDate: new FormControl('', [Validators.required]),
            businessTripType: new FormControl('', [Validators.required]),
            organizationName: new FormControl('', [Validators.minLength(3)]),
            okpoCode: new FormControl('', [Validators.minLength(3)]),
            comment: new FormControl('', []),
            chief: new FormControl('', [])
        });

        this.setBusinessTripType(BusinessTripTypeEnum.DIRECTION);
        this.setCurrentDocumentDate(new Date());
        this.setCurrentReasonDocumentDate(new Date());

        this.initBusinessTripFormValue(this.currentBusinessTripDetailsModel);
        this.initEmployeesTableColumns();
    }

    private initEmployeesTableColumns(): void {
        const employeeColumn: Column = new Column('employee', {
            title: 'Сотрудник',
            type: 'string',
            filter: false,
            sort: false,
            editor: {
                type: 'custom',
                component: EmployeeEditComponent,
            },
            valuePrepareFunction(a: any, value: BusinessTripEmployeeModel, cell: Cell) {
                return Utils.shortInitials(value.employee.initials);
            }
        }, undefined);

        const postColumn: Column = new Column('employeePost', {
            title: 'Должность',
            type: 'string',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
            valuePrepareFunction(a: any, value: BusinessTripEmployeeModel, cell: Cell) {
                return value.employee.postName;
            }
        }, undefined);

        const departmentColumn: Column = new Column('employeeDepartment', {
            title: 'Подразделние',
            type: 'string',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
            valuePrepareFunction(a: any, value: BusinessTripEmployeeModel, cell: Cell) {
                return value.employee.departmentName;
            }
        }, undefined);

        const dateFromColumn = new Column('businessTripFrom', {
            title: 'Начало',
            type: 'date',
            editable: true,
            addable: true,
            filter: false,
            sort: false,
            editor: {
                type: 'custom',
                config: {},
                component: BusinessTripDetailsEmployeeFromEditComponent,
            },
            valuePrepareFunction(value: string) {
                return Utils.getDateFormatFromString(value);
            }
        }, undefined);

        const dateToColumn = new Column('businessTripTo', {
            title: 'Конец',
            type: 'date',
            editable: true,
            addable: true,
            filter: false,
            sort: false,
            editor: {
                type: 'custom',
                config: {},
                component: BusinessTripDetailsEmployeeToEditComponent,
            },
            valuePrepareFunction(value: string) {
                return Utils.getDateFormatFromString(value);
            }
        }, undefined);


        const daysColumn: Column = new Column('businessTripDays', {
            title: 'Дней',
            type: 'number',
            editable: true,
            addable: true,
            filter: false,
            sort: false,
            editor: {
                type: 'custom',
                config: {},
                component: BusinessTripDetailsEmployeeDaysEditComponent
            }
        }, undefined);

        const destinationOrganizationNameColumn: Column = new Column('destinationOrganizationName', {
            title: 'Организация',
            type: 'string',
            editable: true,
            addable: true,
            filter: false,
            sort: false
        }, undefined);

        const destinationCountryColumn: Column = new Column('destinationCountry', {
            title: 'Страна назначения',
            type: 'string',
            editable: true,
            addable: true,
            filter: false,
            sort: false
        }, undefined);

        const destinationCityColumn: Column = new Column('destinationCity', {
            title: 'Город назначения',
            type: 'string',
            editable: true,
            addable: true,
            filter: false,
            sort: false
        }, undefined);

        const targetDescriptionColumn: Column = new Column('targetDescription', {
            title: 'Цель',
            type: 'string',
            editable: true,
            addable: true,
            filter: false,
            sort: false
        }, undefined);

        const sourceOfFinancingColumn: Column = new Column('sourceOfFinancing', {
            title: 'Источник финансирования',
            type: 'string',
            editable: true,
            addable: true,
            filter: false,
            sort: false
        }, undefined);

        this.employeeColumns.push(
            employeeColumn,
            postColumn,
            departmentColumn,
            dateFromColumn,
            dateToColumn,
            daysColumn,
            destinationOrganizationNameColumn,
            destinationCountryColumn,
            destinationCityColumn,
            targetDescriptionColumn,
            sourceOfFinancingColumn
        );
    }

    private initEmployeesTable(businessTripDetails: BusinessTripDetailsModel): void {
        this.employeeDataSource = new BusinessTripDetailsEmployeeDataSource(
            this.businessTripService,
            businessTripDetails.id
        );

        this.employeeDataSource
            .onLoading()
            .pipe(takeUntil(this.destroySubjects))
            .subscribe(loading => this.employeesLoading = loading);
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
            this.formBusinessTrip.controls['chief'].setValue(businessTripDetails.chiefEmployee);

            this.initReason(this.hasReason());
        }
    }

    searchEmployee(event) {
        this.employeeService.findAllEmployees(FindEmployeeRequestModel.findAll(event.query))
            .subscribe(employeePage => this.employeesResult = employeePage.data);
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

    selectBusinessTripType(selectBusinessTripType: BusinessTripTypeModel) {
        this.selectedBusinessTripType = selectBusinessTripType;

        this.initReason(this.hasReason());
    }

    ngOnDestroy() {
        this.destroySubjects.next(true);
        this.destroySubjects.complete();
    }

    hasReason(): boolean {
        return this.selectedBusinessTripType &&
            this.selectedBusinessTripType.businessTripType !== BusinessTripTypeEnum.DIRECTION;
    }

    submitBusinessTripForm() {
        const documentDateStructure: NgbDateStruct = this.formBusinessTrip.controls['documentDate'].value;

        const documentDate = new Date(
            documentDateStructure.year,
            documentDateStructure.month - 1,
            documentDateStructure.day
        );

        const businessTripType = this.formBusinessTrip.controls['businessTripType'].value.businessTripType;
        const documentNumber = this.formBusinessTrip.controls['documentNumber'].value;
        const organizationName = this.formBusinessTrip.controls['organizationName'].value;
        const okpoCode = this.formBusinessTrip.controls['okpoCode'].value;
        const comment = this.formBusinessTrip.controls['comment'].value;

        const chiefEmployeeId = this.formBusinessTrip.controls['chief'].value ?
            this.formBusinessTrip.controls['chief'].value.id : null;

        let reasonDocumentDate;
        let reasonDescription;
        let reasonDocumentNumber;

        if (businessTripType !== BusinessTripTypeEnum.DIRECTION) {
            const reasonDocumentDateStructure: NgbDateStruct = this.formBusinessTrip.controls['reasonDocumentDate'].value;

            reasonDocumentDate = new Date(
                reasonDocumentDateStructure.year,
                reasonDocumentDateStructure.month - 1,
                reasonDocumentDateStructure.day
            );

            reasonDescription = this.formBusinessTrip.controls['reasonDescription'].value;
            reasonDocumentNumber = this.formBusinessTrip.controls['reasonDocumentNumber'].value;
        }

        if (this.currentBusinessTripDetailsModel.id) {
            const updateRequest: BusinessTripEditRequestModel = new BusinessTripEditRequestModel();
            updateRequest.businessTripType = businessTripType;
            updateRequest.documentDate = Utils.getDateToSend(documentDate);
            updateRequest.documentNumber = documentNumber;
            updateRequest.organizationName = organizationName;
            updateRequest.okpoCode = okpoCode;
            updateRequest.comment = comment;
            updateRequest.chiefEmployeeId = chiefEmployeeId;
            updateRequest.reasonDocumentDate = Utils.getDateToSend(reasonDocumentDate);
            updateRequest.reasonDescription = reasonDescription;
            updateRequest.reasonDocumentNumber = reasonDocumentNumber;

            this.businessTripService.updateBusinessTrip(this.currentBusinessTripDetailsModel.id, updateRequest)
                .pipe(takeUntil(this.destroySubjects))
                .subscribe((updatableBusinessTrip: BusinessTripDetailsModel) =>
                    this.currentBusinessTripDetailsModel = updatableBusinessTrip);
        } else {
            const createRequest: BusinessTripCreateRequestModel = new BusinessTripCreateRequestModel();
            createRequest.businessTripType = businessTripType;
            createRequest.documentDate = Utils.getDateToSend(documentDate);
            createRequest.documentNumber = documentNumber;
            createRequest.organizationName = organizationName;
            createRequest.okpoCode = okpoCode;
            createRequest.comment = comment;
            createRequest.chiefEmployeeId = chiefEmployeeId;
            createRequest.reasonDocumentDate = Utils.getDateToSend(reasonDocumentDate);
            createRequest.reasonDescription = reasonDescription;
            createRequest.reasonDocumentNumber = reasonDocumentNumber;

            this.businessTripService.createBusinessTrip(createRequest)
                .pipe(takeUntil(this.destroySubjects))
                .subscribe(businessTripId => {
                    const url = Utils.createUrlFromUrlTemplate(
                        RouterClientConstant.BUSINESS_TRIP_ID_URL,
                        PathVariableConstant.BUSINESS_TRIP_ID,
                        businessTripId.toString()
                    );

                    this.router.navigate([url]);
                });
        }
    }

    removeEmployee(event: TableEventDeleteModel<BusinessTripEmployeeModel>) {
        console.log(event);
    }

    addEmployee(event: TableEventAddModel<BusinessTripEmployeeModel>) {
        console.log(event.newData);
    }

    editEmployee(event: TableEventEditModel<BusinessTripEmployeeModel>) {
        console.log(event);
    }

}

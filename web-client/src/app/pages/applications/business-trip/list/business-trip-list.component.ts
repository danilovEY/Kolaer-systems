import {Component, OnDestroy, OnInit, ViewChild} from "@angular/core";
import {BusinessTripService} from "../service/business-trip.service";
import {Title} from "@angular/platform-browser";
import {CustomTableComponent} from "../../../../@theme/components";
import {Column} from "ng2-smart-table/lib/data-set/column";
import {CustomActionModel} from "../../../../@theme/components/table/custom-action.model";
import {BusinessTripListDataSource} from "./business-trip-list.data-source";
import {Utils} from "../../../../@core/utils/utils";
import {CustomActionEventModel} from "../../../../@theme/components/table/custom-action-event.model";
import {SmartTableService} from "../../../../@core/services/smart-table.service";
import {BusinessTripModel} from "../model/business-trip.model";
import {TableEventAddModel} from "../../../../@theme/components/table/table-event-add.model";
import {AccountService} from "../../../../@core/services/account.service";
import {SimpleAccountModel} from "../../../../@core/models/simple-account.model";
import {RoleConstant} from "../../../../@core/constants/role.constant";
import {Subject} from "rxjs";
import {takeUntil} from "rxjs/operators";
import {Cell} from "ng2-smart-table";
import {BusinessTripEmployeeModel} from "../model/business-trip-employee.model";
import {BusinessTripTypeEnum} from "../model/business-trip-type.enum";
import {Router} from "@angular/router";
import {RouterClientConstant} from "../../../../@core/constants/router-client.constant";
import {PathVariableConstant} from "../../../../@core/constants/path-variable.constant";

@Component({
    selector: 'business-trip-list',
    templateUrl: './business-trip-list.component.html',
    styleUrls: ['./business-trip-list.component.scss']
})
export class BusinessTripListComponent implements OnInit, OnDestroy {
    private static readonly DOWNLOAD_REPORT_ACTION_NAME: string = 'downloadReport';
    private static CURRENT_ACCOUNT: SimpleAccountModel;

    private readonly destroySubjects: Subject<any> = new Subject<any>();

    @ViewChild('businessTripTable')
    businessTripTable: CustomTableComponent;

    businessTripsLoading: boolean = true;
    businessTripColumns: Column[] = [];
    businessTripActions: CustomActionModel[] = [];

    businessTripDataSource: BusinessTripListDataSource;

    private static getNameOfType(type: BusinessTripTypeEnum): string {
        switch (type) {
            case BusinessTripTypeEnum.DIRECTION : return 'Направление';
            case BusinessTripTypeEnum.CHANGE : return 'Изменение';
            case BusinessTripTypeEnum.CANCEL : return 'Отмена';
            default: return 'Неизвестный тип';
        }
    }

    constructor(private businessTripService: BusinessTripService,
                private accountService: AccountService,
                private router: Router,
                private titleService: Title) {
        this.titleService.setTitle('Список командировок');

        this.businessTripDataSource = new BusinessTripListDataSource(this.businessTripService);
        this.businessTripDataSource.onLoading().subscribe(loading => this.businessTripsLoading = loading);
    }

    ngOnInit() {
        this.accountService.getCurrentAccount()
            .pipe(takeUntil(this.destroySubjects))
            .subscribe((currentAccount: SimpleAccountModel) => {
                BusinessTripListComponent.CURRENT_ACCOUNT = currentAccount;

                this.businessTripTable.table.initGrid();
            });

        const documentDataColumn = new Column('documentDate', {
            title: 'Дата',
            type: 'string',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
            valuePrepareFunction(a: any, value: BusinessTripModel, cell: Cell) {
                return Utils.getDateFormat(value.documentDate);
            }
        }, undefined);

        const documentNumberColumn = new Column('documentNumber', {
            title: 'Номер',
            type: 'string',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
        }, undefined);

        const businessTripTypeColumn = new Column('documentDate', {
            title: 'Тип',
            type: 'string',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
            valuePrepareFunction(a: any, value: BusinessTripModel, cell: Cell) {
                return value ? BusinessTripListComponent.getNameOfType(value.businessTripType) : '';
            }
        }, undefined);

        const destinationsColumn = new Column('destinations', {
            title: 'Пункты назначения',
            type: 'html',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
            valuePrepareFunction(a: any, value: BusinessTripModel, cell: Cell) {
                return value
                    ? value
                        .employees
                        .map((employee: BusinessTripEmployeeModel) => employee.destinationCity)
                        .join(',<br />')
                    : '';
            }
        }, undefined);

        const employeesColumn = new Column('employees', {
            title: 'Сотрудники',
            type: 'html',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
            valuePrepareFunction(a: any, value: BusinessTripModel, cell: Cell) {
                return value
                    ? value
                        .employees
                        .map((employee: BusinessTripEmployeeModel) => Utils.shortInitials(employee.employee.initials))
                        .join(',<br />')
                    : '';
            }
        }, undefined);

        const businessTripDatesColumn = new Column('businessTripDates', {
            title: 'Сроки',
            type: 'html',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
            valuePrepareFunction(a: any, value: BusinessTripModel, cell: Cell) {
                return value
                    ? value
                        .employees
                        .map((employee: BusinessTripEmployeeModel) =>
                            Utils.getShortDateFormat(employee.businessTripFrom) +
                            ' - ' +
                            Utils.getShortDateFormat(employee.businessTripTo))
                        .join(',<br />')
                    : '';
            }
        }, undefined);


        const writerEmployeeColumn = new Column('writerEmployee', {
            title: 'Ответственный',
            type: 'text',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
            valuePrepareFunction(a: any, value: BusinessTripModel, cell: Cell) {
                return value && value.writerEmployee
                    ? value.writerEmployee.initials
                    : '';
            }
        }, undefined);

        const commentColumn = new Column('comment', {
            title: 'Комментарий',
            type: 'string',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
        }, undefined);

        this.businessTripColumns.push(
            documentDataColumn,
            documentNumberColumn,
            businessTripTypeColumn,
            destinationsColumn,
            employeesColumn,
            businessTripDatesColumn,
            writerEmployeeColumn,
            commentColumn
        );

        const downloadReportAction: CustomActionModel = new CustomActionModel();
        downloadReportAction.name = BusinessTripListComponent.DOWNLOAD_REPORT_ACTION_NAME;
        downloadReportAction.content = '<i class="fa fa-download"></i>';
        downloadReportAction.description = 'Скачать отчет';

        this.businessTripActions.push(downloadReportAction);

        this.businessTripTable.actionBeforeValueView = this.actionBeforeValueView;
    }

    actionBeforeValueView(event: CustomActionEventModel<BusinessTripModel>) {
        if (event.action.name === SmartTableService.EDIT_ACTION_NAME ||
            event.action.name === SmartTableService.DELETE_ACTION_NAME) {
                return BusinessTripListComponent.CURRENT_ACCOUNT
                    ? BusinessTripListComponent.CURRENT_ACCOUNT.access.includes(RoleConstant.BUSINESS_TRIP_WRITE)
                    : false;
        }

        return true;
    }

    create(event: TableEventAddModel<BusinessTripListDataSource>) {
        this.router.navigate([RouterClientConstant.BUSINESS_TRIP_CREATE_URL]);
    }

    action(event: CustomActionEventModel<BusinessTripModel>) {
        if (event.action.name === SmartTableService.EDIT_ACTION_NAME) {
            const url: string = Utils.createUrlFromUrlTemplate(
                RouterClientConstant.BUSINESS_TRIP_ID_URL,
                PathVariableConstant.BUSINESS_TRIP_ID,
                event.data.id.toString()
            );

            this.router.navigate([url]);
        }
    }

    ngOnDestroy() {
        this.destroySubjects.next(true);
        this.destroySubjects.complete();
    }

}

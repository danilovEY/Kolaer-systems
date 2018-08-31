import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {DepartmentModel} from '../../../../../@core/models/department.model';
import {SimpleAccountModel} from '../../../../../@core/models/simple-account.model';
import {NgbDate} from '@ng-bootstrap/ng-bootstrap/datepicker/ngb-date';
import {ToasterConfig, ToasterService} from 'angular2-toaster';
import {NgbCalendar, NgbDateStruct} from '@ng-bootstrap/ng-bootstrap';
import {DepartmentSortModel} from '../../../../../@core/models/department-sort.model';
import {SortTypeEnum} from '../../../../../@core/models/sort-type.enum';
import {DepartmentFilterModel} from '../../../../../@core/models/department-filter.model';
import {DepartmentService} from '../../../../../@core/services/department.service';
import {EmployeeService} from '../../../../../@core/services/employee.service';
import {AccountService} from '../../../../../@core/services/account.service';
import {ReportFilterModel} from '../../model/report-filter.model';

@Component({
    selector: 'report-filter',
    templateUrl: './report-filter.component.html',
    styleUrls: ['./report-filter.component.scss']
})
export class ReportFilterComponent implements OnInit {
    readonly filterModel: ReportFilterModel = new ReportFilterModel();

    @Output()
    onChangeFilter = new EventEmitter<ReportFilterModel>();

    @Output()
    onAcceptFilter = new EventEmitter<ReportFilterModel>();

    @Input()
    maxMonth: number = 0;

    @Input()
    active: boolean = true;

    @Input()
    pipeCharts: boolean = false;

    currentAccount: SimpleAccountModel;

    departments: DepartmentModel[] = [];

    hoveredDate: NgbDate;

    fromDate: NgbDate;
    toDate: NgbDate;


    config: ToasterConfig = new ToasterConfig({
        positionClass: 'toast-top-right',
        timeout: 5000,
        newestOnTop: true,
        tapToDismiss: true,
        preventDuplicates: false,
        animation: 'fade',
        limit: 5,
    });

    constructor(private toasterService: ToasterService,
                private calendar: NgbCalendar,
                private departmentService: DepartmentService,
                private accountService: AccountService,
                private employeeService: EmployeeService) {
        this.fromDate = calendar.getToday();
        this.toDate = calendar.getNext(calendar.getToday(), 'd', 3);
    }

    ngOnInit() {
        this.accountService.getCurrentAccount()
            .subscribe(account => {
                this.currentAccount = account;

                if (account.accessVacationAdmin) {
                    const sort = new DepartmentSortModel();
                    sort.sortAbbreviatedName = SortTypeEnum.ASC;

                    this.departmentService.getAllDepartments(sort, new DepartmentFilterModel(), 1, 1000)
                        .subscribe(depPage => this.departments = depPage.data);
                } else {
                    this.employeeService.getCurrentEmployee()
                        .subscribe(employee => this.selectDepartment([employee.department]));
                }
            });
    }

    selectDepartment(multiSelect: any) {
        this.filterModel.selectedDepartments = multiSelect.value;
        if (this.onChangeFilter) {
            this.onChangeFilter.emit(this.filterModel);
        }
    }

    onDateSelection(date: NgbDate) {
        if (!this.fromDate && !this.toDate) {
            this.fromDate = date;
        } else if (this.fromDate && !this.toDate &&
            this.isAfterNgbDate(date, this.fromDate) &&
            this.isRangeNgbDate(this.fromDate, date, this.maxMonth)) {
            this.toDate = date;
        } else {
            this.toDate = null;
            this.fromDate = date;
        }

        this.filterModel.from = this.convertToDate(this.fromDate);
        this.filterModel.to = this.convertToDate(this.toDate);

        if (this.onChangeFilter) {
            this.onChangeFilter.emit(this.filterModel);
        }
    }

    onSelectedAllDepartment(selected: Event) {
        this.filterModel.selectedAllDepartments = selected.returnValue;
        if (this.onChangeFilter) {
            this.onChangeFilter.emit(this.filterModel);
        }
    }

    onPipeCharts(selected: Event) {
        this.filterModel.pipeCharts = selected.returnValue;
        if (this.onChangeFilter) {
            this.onChangeFilter.emit(this.filterModel);
        }
    }

    generateReport() {
        if (this.onAcceptFilter) {
            this.onAcceptFilter.emit(this.filterModel);
        }
    }

    private convertToDate(ngbDate: NgbDate): Date {
        return ngbDate
            ? new Date(ngbDate.year, ngbDate.month - 1, ngbDate.day)
            : undefined;
    }

    isHovered(date: NgbDate) {
        return this.fromDate && !this.toDate &&
            this.hoveredDate && this.isAfterNgbDate(date, this.fromDate) &&
            this.isBeforeNgbDate(date, this.hoveredDate) && this.isRangeNgbDate(this.fromDate, this.hoveredDate, this.maxMonth);
    }

    isInside(date: NgbDate) {
        return this.isAfterNgbDate(date, this.fromDate) && this.isBeforeNgbDate(date, this.toDate);
    }

    isRange(date: NgbDate) {
        return this.isEqualsNgbDate(date, this.fromDate) ||
            this.isEqualsNgbDate(date, this.toDate) || this.isInside(date) || this.isHovered(date);
    }

    isFrom(date) {
        return this.isEqualsNgbDate(date, this.fromDate);
    }

    isTo(date) {
        this.isEqualsNgbDate(date, this.toDate);
    }

    isRangeNgbDate = (one: NgbDateStruct, two: NgbDateStruct, month: number) =>
        this.maxMonth > 0
            ? two.year - one.year === 0 && two.month - one.month < month
            : true;

    isEqualsNgbDate = (one: NgbDateStruct, two: NgbDateStruct) =>
        one && two && two.year === one.year && two.month === one.month && two.day === one.day;

    isBeforeNgbDate = (one: NgbDateStruct, two: NgbDateStruct) =>
        !one || !two ? false : one.year === two.year ? one.month === two.month ? one.day === two.day
            ? false : one.day < two.day : one.month < two.month : one.year < two.year;

    isAfterNgbDate = (one: NgbDateStruct, two: NgbDateStruct) =>
        !one || !two ? false : one.year === two.year ? one.month === two.month ? one.day === two.day
            ? false : one.day > two.day : one.month > two.month : one.year > two.year;
}

import {Component, ElementRef, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {NbThemeService} from '@nebular/theme';
import {VacationService} from '../../vacation.service';
import {EmployeeService} from '../../../../../@core/services/employee.service';
import {AccountService} from '../../../../../@core/services/account.service';
import {VacationReportDistributeModel} from '../../model/vacation-report-distribute.model';
import {DepartmentModel} from '../../../../../@core/models/department.model';
import {SimpleAccountModel} from '../../../../../@core/models/simple-account.model';
import {ToasterConfig, ToasterService} from 'angular2-toaster';
import {SortTypeEnum} from '../../../../../@core/models/sort-type.enum';
import {DepartmentSortModel} from '../../../../../@core/models/department-sort.model';
import {DepartmentFilterModel} from '../../../../../@core/models/department-filter.model';
import {DepartmentService} from '../../../../../@core/services/department.service';
import {NgbDate} from '@ng-bootstrap/ng-bootstrap/datepicker/ngb-date';
import {NgbCalendar, NgbDateStruct} from '@ng-bootstrap/ng-bootstrap';
import {GenerateReportDistributeRequestModel} from '../../model/generate-report-distribute-request.model';

@Component({
    selector: 'vacation-report-distribute',
    templateUrl: './vacation-report-distribute.component.html',
    styleUrls: ['./vacation-report-distribute.component.scss']
})
export class VacationReportDistributeComponent implements OnInit, OnDestroy {
    @ViewChild('filterElement')
    filterElement: ElementRef;

    vacationReportDistribute: VacationReportDistributeModel;

    currentAccount: SimpleAccountModel;

    departments: DepartmentModel[] = [];
    selectedDepartment: DepartmentModel;

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

    colorScheme: any;
    themeSubscription: any;
    pipeView: number[] = [800, 400];
    lineView: number[] = [800, 300];

    constructor(private theme: NbThemeService,
                private vacationService: VacationService,
                private employeeService: EmployeeService,
                private departmentService: DepartmentService,
                private accountService: AccountService,
                private toasterService: ToasterService,
                private calendar: NgbCalendar) {
        this.fromDate = calendar.getToday();
        this.toDate = calendar.getNext(calendar.getToday(), 'm', 2);

        this.themeSubscription = this.theme.getJsTheme().subscribe(config => {
            const colors: any = config.variables;
            this.colorScheme = {
                domain: [colors.primaryLight, colors.infoLight, colors.successLight, colors.warningLight, colors.dangerLight],
            };
        });
    }

    ngOnInit(): void {
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
                        .subscribe(employee => this.selectDepartment(employee.department));
                }
            });
    }

    generateReport() {

    }

    selectDepartment(event: DepartmentModel) {
        this.selectedDepartment = event;
        this.generateReport();
    }

    generateReportDistribute() {
        if (this.selectedDepartment && this.fromDate && this.toDate) {
            const request = new GenerateReportDistributeRequestModel();
            request.addPipes = true;
            request.departmentIds = [this.selectedDepartment.id];
            request.from = new Date(this.fromDate.year, this.fromDate.month - 1, this.fromDate.day);
            request.to = new Date(this.toDate.year, this.toDate.month - 1, this.toDate.day);

            this.vacationService.generateVacationReportDistribute(request)
                .subscribe(vacationReport => {
                    this.vacationReportDistribute = vacationReport;
                    this.pipeView[0] = this.filterElement.nativeElement.offsetWidth - 70;
                    this.lineView[0] = this.filterElement.nativeElement.offsetWidth - 70;
                });
        }
    }

    onDateSelection(date: NgbDate) {
        if (!this.fromDate && !this.toDate) {
            this.fromDate = date;
        } else if (this.fromDate && !this.toDate &&
            this.isAfterNgbDate(date, this.fromDate) &&
            this.isRangeNgbDate(this.fromDate, date, 12)) {
            this.toDate = date;
        } else {
            this.toDate = null;
            this.fromDate = date;
        }
    }

    isHovered(date: NgbDate) {
        return this.fromDate && !this.toDate &&
            this.hoveredDate && this.isAfterNgbDate(date, this.fromDate) &&
            this.isBeforeNgbDate(date, this.hoveredDate) && this.isRangeNgbDate(this.fromDate, this.hoveredDate, 12);
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
        two.year - one.year === 0 && two.month - one.month < month;

    isEqualsNgbDate = (one: NgbDateStruct, two: NgbDateStruct) =>
        one && two && two.year === one.year && two.month === one.month && two.day === one.day;

    isBeforeNgbDate = (one: NgbDateStruct, two: NgbDateStruct) =>
        !one || !two ? false : one.year === two.year ? one.month === two.month ? one.day === two.day
            ? false : one.day < two.day : one.month < two.month : one.year < two.year;

    isAfterNgbDate = (one: NgbDateStruct, two: NgbDateStruct) =>
        !one || !two ? false : one.year === two.year ? one.month === two.month ? one.day === two.day
            ? false : one.day > two.day : one.month > two.month : one.year > two.year;

    ngOnDestroy(): void {
        this.themeSubscription.unsubscribe();
    }
}

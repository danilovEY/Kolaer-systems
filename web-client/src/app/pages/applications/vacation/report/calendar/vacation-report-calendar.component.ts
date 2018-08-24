import {Component, OnInit} from '@angular/core';
import {VacationService} from '../../vacation.service';
import {DepartmentService} from '../../../../../@core/services/department.service';
import {AccountService} from '../../../../../@core/services/account.service';
import {VacationReportCalendarEmployeeModel} from '../../model/vacation-report-calendar-employee.model';
import {GenerateReportCalendarRequestModel} from '../../model/generate-report-calendar-request.model';
import {SimpleAccountModel} from '../../../../../@core/models/simple-account.model';
import {DepartmentModel} from '../../../../../@core/models/department.model';
import {EmployeeService} from '../../../../../@core/services/employee.service';
import {DepartmentFilterModel} from '../../../../../@core/models/department-filter.model';
import {SortTypeEnum} from '../../../../../@core/models/sort-type.enum';
import {DepartmentSortModel} from '../../../../../@core/models/department-sort.model';
import {NgbDate} from '@ng-bootstrap/ng-bootstrap/datepicker/ngb-date';
import {NgbCalendar, NgbDateStruct} from '@ng-bootstrap/ng-bootstrap';
import {ToasterConfig, ToasterService} from 'angular2-toaster';

@Component({
    selector: 'vacation-report',
    templateUrl: './vacation-report-calendar.component.html',
    styleUrls: ['./vacation-report-calendar.component.scss']
})
export class VacationReportCalendarComponent implements OnInit {

    vacationReportCalendarData: VacationReportCalendarEmployeeModel[];

    columnYears: any[] = [];
    columnMonths: any[] = [];
    columnDays: string[] = [];

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

    constructor(private vacationService: VacationService,
                private departmentService: DepartmentService,
                private accountService: AccountService,
                private toasterService: ToasterService,
                private employeeService: EmployeeService,
                private calendar: NgbCalendar) {
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
                        .subscribe(employee => this.selectDepartment(employee.department));
                }
            });
    }

    updateReportCalendarColumns(): void {
        this.columnYears = [];
        this.columnMonths = [];
        this.columnDays = [];

        if (this.vacationReportCalendarData && this.vacationReportCalendarData.length > 0) {
            for (const year of this.vacationReportCalendarData[0].years) {
                let sizeDays: number = 0;

                for (const month of year.months) {
                    sizeDays += month.days.length;
                }

                const columnYear: any = {
                    name: year.year,
                    size: sizeDays
                };

                this.columnYears.push(columnYear);

                for (const month of year.months) {
                    const columnMonth: any = {
                        name: month.month,
                        size: month.days.length
                    };

                    this.columnMonths.push(columnMonth);

                    for (const day of month.days) {
                        this.columnDays.push(day.day);
                    }
                }
            }
        }
    }

    generateReport() {

    }

    selectDepartment(event: DepartmentModel) {
        this.selectedDepartment = event;
        this.generateReport();
    }

    generateReportCalendar() {
        if (this.selectedDepartment && this.fromDate && this.toDate) {
            const request = new GenerateReportCalendarRequestModel();
            request.departmentId = this.selectedDepartment.id;
            request.from = new Date(this.fromDate.year, this.fromDate.month - 1, this.fromDate.day);
            request.to = new Date(this.toDate.year, this.toDate.month - 1, this.toDate.day);

            this.vacationService.generateVacationReportCalendar(request)
                .subscribe(vacationReport => {
                    this.vacationReportCalendarData = vacationReport;
                    this.updateReportCalendarColumns();
                });
        }
    }

    onDateSelection(date: NgbDate) {
        if (!this.fromDate && !this.toDate) {
            this.fromDate = date;
        } else if (this.fromDate && !this.toDate &&
            this.isAfterNgbDate(date, this.fromDate) &&
            this.isRangeNgbDate(this.fromDate, date, 3)) {
            this.toDate = date;
        } else {
            this.toDate = null;
            this.fromDate = date;
        }
    }

    isHovered(date: NgbDate) {
        return this.fromDate && !this.toDate &&
            this.hoveredDate && this.isAfterNgbDate(date, this.fromDate) &&
            this.isBeforeNgbDate(date, this.hoveredDate) && this.isRangeNgbDate(this.fromDate, this.hoveredDate, 3);
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
}

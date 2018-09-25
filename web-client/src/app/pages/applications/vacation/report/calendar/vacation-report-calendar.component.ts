import {Component, OnInit} from '@angular/core';
import {VacationService} from '../../vacation.service';
import {VacationReportCalendarEmployeeModel} from '../../model/vacation-report-calendar-employee.model';
import {GenerateReportCalendarRequestModel} from '../../model/generate-report-calendar-request.model';
import {ReportFilterModel} from '../../model/report-filter.model';
import {Title} from '@angular/platform-browser';
import {DepartmentSortModel} from '../../../../../@core/models/department-sort.model';
import {SortTypeEnum} from '../../../../../@core/models/sort-type.enum';
import {DepartmentFilterModel} from '../../../../../@core/models/department-filter.model';
import {DepartmentService} from '../../../../../@core/services/department.service';
import {EmployeeService} from '../../../../../@core/services/employee.service';
import {AccountService} from '../../../../../@core/services/account.service';
import {SimpleAccountModel} from '../../../../../@core/models/simple-account.model';
import {DepartmentModel} from '../../../../../@core/models/department.model';
import * as html2canvas from 'html2canvas';

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

    filterModel: ReportFilterModel = new ReportFilterModel();

    currentAccount: SimpleAccountModel;
    departments: DepartmentModel[] = [];

    constructor(private vacationService: VacationService,
                private departmentService: DepartmentService,
                private accountService: AccountService,
                private employeeService: EmployeeService,
                private titleService: Title) {
        this.titleService.setTitle('График пересечений');
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
                        .subscribe(employee => this.filterModel.selectedDepartments = [employee.department]);
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

    changeFilter(filterModel: ReportFilterModel) {
        this.filterModel = filterModel;
    }

    generateReportCalendar(filterModel: ReportFilterModel) {
        if ((this.filterModel.selectedDepartments.length > 0 || this.filterModel.selectedAllDepartments)
            && this.filterModel.from && this.filterModel.to) {
            const request = new GenerateReportCalendarRequestModel();
            request.departmentIds = this.filterModel.selectedDepartments.map(dep => dep.id);
            request.allDepartment = this.filterModel.selectedAllDepartments;
            request.from = this.filterModel.from;
            request.to = this.filterModel.to;

            this.vacationService.generateVacationReportCalendar(request)
                .subscribe(vacationReport => {
                    this.vacationReportCalendarData = vacationReport;
                    this.updateReportCalendarColumns();
                });
        }
    }

    downloadCalendarChart() {
        this.downloadElement(document.getElementById('calendarChart'), 'calendar_sootnoshenij.png');
    }

    private downloadElement(data: HTMLElement, name: string): void {
        // const h = data;
        // data[0].nativeElement.defaultView.innerWidth = data.offsetWidth;

        html2canvas(data, {
            allowTaint: true,
            logging: false
        }).then(canvas => {
            const contentDataURL = canvas.toDataURL('image/png');

            // data[0].nativeElement.defaultView.innerWidth = h;

            const a = document.createElement('a');
            document.body.appendChild(a);
            a.setAttribute('style', 'display: none');
            a.href = contentDataURL;
            a.download = name;
            a.click();
            window.URL.revokeObjectURL(contentDataURL);
            a.remove();
        });
    }
}

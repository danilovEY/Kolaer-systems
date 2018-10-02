import {Component, OnInit} from '@angular/core';
import {VacationService} from '../../vacation.service';
import {VacationReportCalendarEmployeeModel} from '../../model/vacation-report-calendar-employee.model';
import {GenerateReportCalendarRequestModel} from '../../model/generate-report-calendar-request.model';
import {ReportFilterModel} from '../../model/report-filter.model';
import {Title} from '@angular/platform-browser';
import {DepartmentService} from '../../../../../@core/services/department.service';
import {EmployeeService} from '../../../../../@core/services/employee.service';
import {AccountService} from '../../../../../@core/services/account.service';
import {SimpleAccountModel} from '../../../../../@core/models/simple-account.model';
import {VacationReportCalendarDayModel} from '../../model/vacation-report-calendar-day.model';
import {Toast, ToasterConfig, ToasterService} from 'angular2-toaster';

@Component({
    selector: 'vacation-report',
    templateUrl: './vacation-report-calendar.component.html',
    styleUrls: ['./vacation-report-calendar.component.scss']
})
export class VacationReportCalendarComponent implements OnInit {

    vacationReportCalendarData: VacationReportCalendarEmployeeModel[];

    widthColumn: number = 5;
    scrollTable = true;

    columnYears: any[] = [];
    columnMonths: any[] = [];
    columnDays: string[] = [];

    filterModel: ReportFilterModel = new ReportFilterModel();

    currentAccount: SimpleAccountModel;

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
                private employeeService: EmployeeService,
                private toasterService: ToasterService,
                private titleService: Title) {
        this.titleService.setTitle('График пересечений');
    }

    ngOnInit() {
        this.accountService.getCurrentAccount()
            .subscribe(account => {
                this.currentAccount = account;

                if (!account.accessVacationAdmin) {
                    this.employeeService.getCurrentEmployee()
                        .subscribe(employee => this.filterModel.selectedDepartment = employee.department);
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
        const request = new GenerateReportCalendarRequestModel();
        request.departmentIds = this.filterModel.selectedDepartment ? [this.filterModel.selectedDepartment.id] : [];
        request.employeeIds = this.filterModel.selectedEmployees.map(emp => emp.id);
        request.postIds = this.filterModel.selectedPosts.map(post => post.id);
        request.typeWorkIds = this.filterModel.selectedTypeWorks.map(typeWork => typeWork.id);
        request.allDepartment = this.filterModel.selectedAllDepartments;
        request.from = this.filterModel.from;
        request.to = this.filterModel.to;

        this.vacationService.generateVacationReportCalendar(request)
            .subscribe(vacationReport => {
                this.vacationReportCalendarData = vacationReport;
                this.updateReportCalendarColumns();
            });
    }

    downloadCalendarChart() {
        const request = new GenerateReportCalendarRequestModel();
        request.departmentIds = this.filterModel.selectedDepartment ? [this.filterModel.selectedDepartment.id] : [];
        request.employeeIds = this.filterModel.selectedEmployees.map(emp => emp.id);
        request.postIds = this.filterModel.selectedPosts.map(post => post.id);
        request.typeWorkIds = this.filterModel.selectedTypeWorks.map(typeWork => typeWork.id);
        request.allDepartment = this.filterModel.selectedAllDepartments;
        request.from = this.filterModel.from;
        request.to = this.filterModel.to;

        this.vacationService.generateVacationReportCalendarAndDownload(request)
            .subscribe(res => {
                const url = window.URL.createObjectURL(res);
                const a = document.createElement('a');
                document.body.appendChild(a);
                a.setAttribute('style', 'display: none');
                a.href = url;
                a.download = 'График пересечений.xlsx';
                a.click();
                window.URL.revokeObjectURL(url);
                a.remove();
            }, responseError => {
                if (responseError.status === 404) {
                    const toast: Toast = {
                        type: 'warning',
                        title: 'Ошибка в операции',
                        body: 'Отпуска не найдены'
                    };

                    this.toasterService.popAsync(toast);
                } else {
                    const toast: Toast = {
                        type: 'error',
                        title: 'Ошибка в операции',
                    };

                    this.toasterService.popAsync(toast);
                }
            });

        // this.scrollTable = false;
        //
        // Observable.interval(500)
        //     .takeWhile(() => !this.scrollTable)
        //     .subscribe(i => {
        //         this.downloadElement(document.getElementById('calendarChart'), 'calendar_sootnoshenij.png');
        //     });
    }

    // private downloadElement(data: HTMLElement, name: string): void {
    //     html2canvas(data, {
    //         allowTaint: true,
    //         logging: false
    //     }).then(canvas => {
    //         const contentDataURL = canvas.toDataURL('image/png');
    //
    //         const a = document.createElement('a');
    //         document.body.appendChild(a);
    //         a.setAttribute('style', 'display: none');
    //         a.href = contentDataURL;
    //         a.download = name;
    //         a.click();
    //         window.URL.revokeObjectURL(contentDataURL);
    //         a.remove();
    //     });
    //
    //     this.scrollTable = true;
    // }

    getColumnColor(day: VacationReportCalendarDayModel): string {
        if (day.vacation) {
            return '#00cc0f';
        } else if (day.holiday) {
            return '#ff706b';
        } else if (day.dayOff) {
            return '#cdecff';
        }

        return '';
    }
}

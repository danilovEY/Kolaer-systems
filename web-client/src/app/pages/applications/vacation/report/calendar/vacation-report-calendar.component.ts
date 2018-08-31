import {Component, OnInit} from '@angular/core';
import {VacationService} from '../../vacation.service';
import {VacationReportCalendarEmployeeModel} from '../../model/vacation-report-calendar-employee.model';
import {GenerateReportCalendarRequestModel} from '../../model/generate-report-calendar-request.model';
import {ReportFilterModel} from '../../model/report-filter.model';
import {Title} from '@angular/platform-browser';

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

    filterModel: ReportFilterModel;

    constructor(private vacationService: VacationService,
                private titleService: Title) {
        this.titleService.setTitle('График пересечений');
    }

    ngOnInit() {

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
}

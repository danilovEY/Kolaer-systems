import {Component, OnInit} from '@angular/core';
import {VacationService} from '../vacation.service';
import {DepartmentService} from '../../../../@core/services/department.service';
import {AccountService} from '../../../../@core/services/account.service';
import {VacationReportCalendarEmployeeModel} from '../model/vacation-report-calendar-employee.model';

@Component({
    selector: 'vacation-report',
    templateUrl: './vacation-report.component.html',
    styleUrls: ['./vacation-report.component.scss']
})
export class VacationReportComponent implements OnInit {

    vacationReportCalendarData: VacationReportCalendarEmployeeModel[] = [];

    columnYears: any[] = [];

    columnMonths: any[] = [];

    columnDays: string[] = [];

    constructor(private vacationService: VacationService,
                private departmentService: DepartmentService,
                private accountService: AccountService) {

    }

    ngOnInit() {
        if (this.vacationReportCalendarData.length > 0) {
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

}

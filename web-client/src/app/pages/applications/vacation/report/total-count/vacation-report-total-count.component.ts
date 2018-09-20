import {Component, ElementRef, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {Title} from '@angular/platform-browser';
import {EmployeeService} from '../../../../../@core/services/employee.service';
import {NbThemeService} from '@nebular/theme';
import {AccountService} from '../../../../../@core/services/account.service';
import {DepartmentService} from '../../../../../@core/services/department.service';
import {VacationService} from '../../vacation.service';
import {SimpleAccountModel} from '../../../../../@core/models/simple-account.model';
import {ReportFilterModel} from '../../model/report-filter.model';
import {VacationReportPipeModel} from '../../model/vacation-report-pipe.model';
import {DepartmentModel} from '../../../../../@core/models/department.model';
import {DepartmentFilterModel} from '../../../../../@core/models/department-filter.model';
import {DepartmentSortModel} from '../../../../../@core/models/department-sort.model';
import {SortTypeEnum} from '../../../../../@core/models/sort-type.enum';
import {GenerateReportTotalCountRequestModel} from '../../model/generate-report-total-count-request.model';

@Component({
    selector: 'vacation-report-total-count',
    templateUrl: './vacation-report-total-count.component.html',
    styleUrls: ['./vacation-report-total-count.component.scss']
})
export class VacationReportTotalCountComponent implements OnInit, OnDestroy {
    @ViewChild('filterElement')
    filterElement: ElementRef;

    currentAccount: SimpleAccountModel;

    vacationReportPipes: VacationReportPipeModel[] = [];

    filterModel: ReportFilterModel = new ReportFilterModel();

    pipeView: number[] = [800, 400];

    departments: DepartmentModel[] = [];

    themeSubscription: any;
    colorScheme: any;

    constructor(private theme: NbThemeService,
                private departmentService: DepartmentService,
                private accountService: AccountService,
                private employeeService: EmployeeService,
                private vacationService: VacationService,
                private titleService: Title) {
        this.titleService.setTitle('График соотношений');
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
                        .subscribe(employee => this.filterModel.selectedDepartments = [employee.department]);
                }
            });
    }

    changeFilter(filterModel: ReportFilterModel) {
        this.filterModel = filterModel;
    }

    generateReportDistribute(filterModel: ReportFilterModel) {
        if ((this.filterModel.selectedDepartments.length > 0 || this.filterModel.selectedAllDepartments)
            && this.filterModel.from && this.filterModel.to) {
            const request = new GenerateReportTotalCountRequestModel();
            request.departmentIds = this.filterModel.selectedDepartments.map(dep => dep.id);
            request.allDepartment = this.filterModel.selectedAllDepartments;
            request.from = this.filterModel.from;
            request.to = this.filterModel.to;

            this.vacationService.generateVacationReportTotalCount(request)
                .subscribe(vacationReport => {
                    this.vacationReportPipes = vacationReport;
                    this.pipeView[0] = this.filterElement.nativeElement.offsetWidth - 70;
                });
        }
    }

    ngOnDestroy(): void {
        this.themeSubscription.unsubscribe();
    }

}

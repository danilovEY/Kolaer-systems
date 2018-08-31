import {Component, ElementRef, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {NbThemeService} from '@nebular/theme';
import {VacationService} from '../../vacation.service';
import {VacationReportDistributeModel} from '../../model/vacation-report-distribute.model';
import {GenerateReportDistributeRequestModel} from '../../model/generate-report-distribute-request.model';
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

@Component({
    selector: 'vacation-report-distribute',
    templateUrl: './vacation-report-distribute.component.html',
    styleUrls: ['./vacation-report-distribute.component.scss']
})
export class VacationReportDistributeComponent implements OnInit, OnDestroy {
    @ViewChild('filterElement')
    filterElement: ElementRef;

    currentAccount: SimpleAccountModel;

    vacationReportDistribute: VacationReportDistributeModel;

    filterModel: ReportFilterModel = new ReportFilterModel();

    colorScheme: any;
    themeSubscription: any;
    pipeView: number[] = [800, 400];
    lineView: number[] = [800, 300];

    departments: DepartmentModel[] = [];

    constructor(private theme: NbThemeService,
                private departmentService: DepartmentService,
                private accountService: AccountService,
                private employeeService: EmployeeService,
                private vacationService: VacationService,
                private titleService: Title) {
        this.titleService.setTitle('График распределений');
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
            const request = new GenerateReportDistributeRequestModel();
            request.addPipes = this.filterModel.pipeCharts;
            request.departmentIds = this.filterModel.selectedDepartments.map(dep => dep.id);
            request.allDepartment = this.filterModel.selectedAllDepartments;
            request.from = this.filterModel.from;
            request.to = this.filterModel.to;

            this.vacationService.generateVacationReportDistribute(request)
                .subscribe(vacationReport => {
                    this.vacationReportDistribute = vacationReport;
                    this.pipeView[0] = this.filterElement.nativeElement.offsetWidth - 70;
                    this.lineView[0] = this.filterElement.nativeElement.offsetWidth - 70;
                });
        }
    }

    ngOnDestroy(): void {
        this.themeSubscription.unsubscribe();
    }
}

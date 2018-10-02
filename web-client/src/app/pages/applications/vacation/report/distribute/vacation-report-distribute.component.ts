import {Component, ElementRef, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {NbThemeService} from '@nebular/theme';
import {VacationService} from '../../vacation.service';
import {VacationReportDistributeModel} from '../../model/vacation-report-distribute.model';
import {GenerateReportDistributeRequestModel} from '../../model/generate-report-distribute-request.model';
import {ReportFilterModel} from '../../model/report-filter.model';
import {Title} from '@angular/platform-browser';
import {DepartmentService} from '../../../../../@core/services/department.service';
import {EmployeeService} from '../../../../../@core/services/employee.service';
import {AccountService} from '../../../../../@core/services/account.service';
import {SimpleAccountModel} from '../../../../../@core/models/simple-account.model';
import SvgSaver from 'svgsaver';
import * as html2canvas from 'html2canvas';

@Component({
    selector: 'vacation-report-distribute',
    templateUrl: './vacation-report-distribute.component.html',
    styleUrls: ['./vacation-report-distribute.component.scss']
})
export class VacationReportDistributeComponent implements OnInit, OnDestroy {
    @ViewChild('filterElement')
    filterElement: ElementRef;

    @ViewChild('chart')
    chart: ElementRef;

    private svgSaver = new SvgSaver();

    currentAccount: SimpleAccountModel;

    vacationReportDistribute: VacationReportDistributeModel;

    filterModel: ReportFilterModel = new ReportFilterModel();

    colorScheme: any;
    themeSubscription: any;
    pipeView: number[] = [800, 400];
    lineView: number[] = [800, 300];

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

                if (!account.accessVacationAdmin) {
                    this.employeeService.getCurrentEmployee()
                        .subscribe(employee => this.filterModel.selectedDepartments = [employee.department]);
                }
            });
    }

    changeFilter(filterModel: ReportFilterModel) {
        this.filterModel = filterModel;
    }

    generateReportDistribute(filterModel: ReportFilterModel) {
        const request = new GenerateReportDistributeRequestModel();
        request.calculateIntersections = this.filterModel.calculateIntersections;
        request.addPipesForVacation = this.filterModel.pipeCharts;
        request.departmentIds = this.filterModel.selectedDepartments.map(dep => dep.id);
        request.employeeIds = this.filterModel.selectedEmployees.map(emp => emp.id);
        request.postIds = this.filterModel.selectedPosts.map(post => post.id);
        request.typeWorkIds = this.filterModel.selectedTypeWorks.map(typeWork => typeWork.id);
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

    ngOnDestroy(): void {
        this.themeSubscription.unsubscribe();
    }

    downloadLineChart() {
        this.downloadElement(document.getElementById('lineChart'), 'grafik_raspredelenij.png');
    }

    downloadPipeChart() {
        this.downloadElement(document.getElementById('pipeChart'), 'procentnoe_sootnoshenie.png');
    }

    private downloadElement(data: any, name: string): void { // HTMLElement
        // this.svgSaver.asPng(data, name);

        html2canvas(data, {
            allowTaint: true,
            useCORS: true,
            logging: false
        }).then(canvas => {
            const contentDataURL = canvas.toDataURL('image/png');

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

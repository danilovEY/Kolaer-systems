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
import {GenerateReportTotalCountRequestModel} from '../../model/generate-report-total-count-request.model';
import * as html2canvas from 'html2canvas';
import {Toast, ToasterConfig, ToasterService} from 'angular2-toaster';
import {RoleConstant} from "../../../../../@core/constants/role.constant";

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

    themeSubscription: any;
    colorScheme: any;

    config: ToasterConfig = new ToasterConfig({
        positionClass: 'toast-top-right',
        timeout: 5000,
        newestOnTop: true,
        tapToDismiss: true,
        preventDuplicates: false,
        animation: 'fade',
        limit: 5,
    });

    constructor(private theme: NbThemeService,
                private departmentService: DepartmentService,
                private accountService: AccountService,
                private employeeService: EmployeeService,
                private vacationService: VacationService,
                private toasterService: ToasterService,
                private titleService: Title) {
        this.titleService.setTitle('График соотношений');
        this.themeSubscription = this.theme.getJsTheme().subscribe(config => {
            const colors: any = config.variables;
            this.colorScheme = {
                domain: [colors.primaryLight, '#FFFFFF', colors.successLight, colors.warningLight, colors.dangerLight],
            };
        });
    }

    ngOnInit(): void {
        this.accountService.getCurrentAccount()
            .subscribe(account => {
                this.currentAccount = account;

                if (!account.access.includes(RoleConstant.VACATIONS_READ)) {
                    this.employeeService.getCurrentEmployee()
                        .subscribe(employee => this.filterModel.selectedDepartments = [employee.department]);
                }
            });
    }

    changeFilter(filterModel: ReportFilterModel) {
        this.filterModel = filterModel;
    }

    generateReportDistribute(filterModel: ReportFilterModel) {
        const request = new GenerateReportTotalCountRequestModel();
        request.departmentIds = this.filterModel.selectedAllDepartments ? [] : this.filterModel.selectedDepartments.map(dep => dep.id);
        request.employeeIds = this.filterModel.selectedEmployees.map(emp => emp.id);
        request.postIds = this.filterModel.selectedPosts.map(post => post.id);
        request.typeWorkIds = this.filterModel.selectedTypeWorks.map(typeWork => typeWork.id);
        request.groupByDepartments = this.filterModel.groupByDepartments;
        request.from = this.filterModel.from;
        request.to = this.filterModel.to;

        this.vacationService.generateVacationReportTotalCount(request)
            .subscribe(vacationReport => {
                this.vacationReportPipes = vacationReport;
                this.pipeView[0] = this.filterElement.nativeElement.offsetWidth - 70;
            }, responseError => {
                const toast: Toast = {
                    type: 'error',
                    title: 'Ошибка в операции',
                };

                this.toasterService.popAsync(toast);
            });
    }

    downloadBarChart() {
        this.downloadElement(document.getElementById('barChart'), 'grafik_sootnoshenij.png');
    }

    private downloadElement(data: HTMLElement, name: string): void {
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

    ngOnDestroy(): void {
        this.themeSubscription.unsubscribe();
    }

    canReadAllVacations(): boolean {
        return this.currentAccount.access.includes(RoleConstant.VACATIONS_READ);
    }
}

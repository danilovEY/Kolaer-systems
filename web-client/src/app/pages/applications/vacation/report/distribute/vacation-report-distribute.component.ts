import {Component, ElementRef, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {NbThemeService} from '@nebular/theme';
import {VacationService} from '../../vacation.service';
import {VacationReportDistributeModel} from '../../model/vacation-report-distribute.model';
import {GenerateReportDistributeRequestModel} from '../../model/generate-report-distribute-request.model';
import {ReportFilterModel} from '../../model/report-filter.model';
import {Title} from '@angular/platform-browser';

@Component({
    selector: 'vacation-report-distribute',
    templateUrl: './vacation-report-distribute.component.html',
    styleUrls: ['./vacation-report-distribute.component.scss']
})
export class VacationReportDistributeComponent implements OnInit, OnDestroy {
    @ViewChild('filterElement')
    filterElement: ElementRef;

    vacationReportDistribute: VacationReportDistributeModel;

    filterModel: ReportFilterModel;

    colorScheme: any;
    themeSubscription: any;
    pipeView: number[] = [800, 400];
    lineView: number[] = [800, 300];

    constructor(private theme: NbThemeService,
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

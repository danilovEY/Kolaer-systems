import {Component, OnInit} from '@angular/core';
import {VacationService} from '../../vacation.service';
import {Title} from '@angular/platform-browser';
import {AccountService} from '../../../../../@core/services/account.service';
import {DepartmentService} from '../../../../../@core/services/department.service';
import {EmployeeService} from '../../../../../@core/services/employee.service';
import {SortTypeEnum} from '../../../../../@core/models/sort-type.enum';
import {DepartmentFilterModel} from '../../../../../@core/models/department-filter.model';
import {DepartmentSortModel} from '../../../../../@core/models/department-sort.model';
import {SimpleAccountModel} from '../../../../../@core/models/simple-account.model';
import {DepartmentModel} from '../../../../../@core/models/department.model';
import {ReportFilterModel} from '../../model/report-filter.model';
import {GenerateReportExportRequestModel} from '../../model/generate-report-export-request.model';
import {Toast, ToasterConfig, ToasterService} from 'angular2-toaster';

@Component({
    selector: 'vacation-export',
    templateUrl: './vacation-export.component.html',
    styleUrls: ['./vacation-export.component.scss']
})
export class VacationExportComponent implements OnInit {
    currentAccount: SimpleAccountModel;
    departments: DepartmentModel[] = [];
    filterModel: ReportFilterModel = new ReportFilterModel();

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
                private title: Title) {
        this.title.setTitle('Выгрузка в Excel');
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

    changeFilter(filterModel: ReportFilterModel) {
        this.filterModel = filterModel;
    }

    generateReportExport(filterModel: ReportFilterModel) {
        if (this.filterModel.selectedDepartment != null &&
            this.filterModel.from && this.filterModel.to) {
            const request = new GenerateReportExportRequestModel();
            request.departmentId = this.filterModel.selectedDepartment.id;
            request.from = this.filterModel.from;
            request.to = this.filterModel.to;

            this.vacationService.generateUrlForVacationReportExport(request)
                .subscribe(res => {
                    const url = window.URL.createObjectURL(res);
                    const a = document.createElement('a');
                    document.body.appendChild(a);
                    a.setAttribute('style', 'display: none');
                    a.href = url;
                    a.download = this.filterModel.selectedDepartment.abbreviatedName + ' (Отчет).xlsx';
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
        }
    }
}

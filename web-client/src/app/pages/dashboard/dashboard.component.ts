import {Component, OnInit, ViewChild} from '@angular/core';
import {EmployeeService} from '../../@core/services/employee.service';
import {EmployeeModel} from '../../@core/models/employee.model';
import {OtherEmployeeModel} from '../../@core/models/other-employee.model';
import {ServerExceptionModel} from '../../@core/models/server-exception.model';
import {NbTabComponent, NbTabsetComponent} from '@nebular/theme/components/tabset/tabset.component';
import {finalize} from 'rxjs/internal/operators';
import {environment} from "../../../environments/environment";
import {Title} from "@angular/platform-browser";

@Component({
    selector: 'ngx-dashboard',
    styleUrls: ['./dashboard.component.scss'],
    templateUrl: './dashboard.component.html',
})
export class DashboardComponent implements OnInit {
    readonly employeeBirthdayTabTitle: string = 'Сотрудники КолАЭР';
    readonly otherEmployeeBirthdayTabTitle: string = 'Сотрудники АЭР';

    @ViewChild('birthdayTab')
    birthdayTab: NbTabsetComponent;

    @ViewChild('employeeTab')
    employeeTab: NbTabComponent;

    employeesBirthdayToday: EmployeeModel[] = [];
    otherEmployeesBirthdayToday: OtherEmployeeModel[] = [];
    loadingEmployeesBirthdayToday: boolean = true;
    loadingOtherEmployeesBirthdayToday: boolean = true;

    today: Date = new Date();

    constructor(private employeeService: EmployeeService,
                private titleService: Title
    ) {
        this.titleService.setTitle('Информационная панель');
    }

    ngOnInit() {
        this.birthdayTab.changeTab.asObservable()
            .subscribe((tab: NbTabComponent) => {
                if (tab.tabTitle === this.employeeBirthdayTabTitle) {
                    this.loadingEmployeesBirthdayToday = true;

                    this.employeeService.getEmployeesBirthdayToday()
                        .pipe(finalize(() => {
                            this.loadingEmployeesBirthdayToday = false;
                        }))
                        .subscribe(
                            (employees: EmployeeModel[]) => this.employeesBirthdayToday = employees,
                            (error: ServerExceptionModel) => console.log(error)
                        );
                } else if (tab.tabTitle === this.otherEmployeeBirthdayTabTitle) {
                    this.loadingOtherEmployeesBirthdayToday = true;

                    this.employeeService.getOtherEmployeesBirthdayToday()
                        .pipe(finalize(() => {
                            this.loadingOtherEmployeesBirthdayToday = false;
                        }))
                        .subscribe(
                            (employees: OtherEmployeeModel[]) => {
                                this.otherEmployeesBirthdayToday = employees;
                            },
                            (error: ServerExceptionModel) => console.log(error)
                        );
                }
            });
    }

    getShortNameOrganization(organization: string): string {
        return EmployeeService.getShortNameOrganization(organization);
    }

    encodeUrl(employee: EmployeeModel): string {
        return employee.photo
            ? environment.publicServerUrl + encodeURI(employee.photo)
            : '/assets/images/no_photo.jpg';
    }
}

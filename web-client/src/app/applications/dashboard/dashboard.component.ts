import {Component, OnInit} from '@angular/core';
import {EmployeeModel} from '../../commons/models/employee.model';
import {DashboardService} from './dashboard.service';
import {ServerExceptionModel} from '../../commons/models/server-exception.model';
import {OtherEmployeeModel} from '../../commons/models/other-employee.model';
import 'rxjs/add/operator/do';

@Component({
	selector: 'app-dashboard',
	templateUrl: './dashboard.component.html',
	styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
	employeesBirthdayToday: EmployeeModel[] = [];
	otherEmployeesBirthdayToday: OtherEmployeeModel[] = [];
    loadingEmployeesBirthdayToday: boolean = true;

	today: Date = new Date();

	constructor(private _dashboardService: DashboardService) {

	}

	ngOnInit() {
		this._dashboardService.getEmployeesBirthdayToday()
			.finally(() => {
                this.loadingEmployeesBirthdayToday = false;
			})
			.subscribe(
				(employees: EmployeeModel[]) => this.employeesBirthdayToday = employees,
				(error: ServerExceptionModel) => console.log(error)
			);

		this._dashboardService.getOtherEmployeesBirthdayToday()
			.subscribe(
				(employees: OtherEmployeeModel[]) => this.otherEmployeesBirthdayToday = employees,
				(error: ServerExceptionModel) => console.log(error)
			);
	}

}

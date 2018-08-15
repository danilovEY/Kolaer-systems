import {Component, OnInit} from '@angular/core';
import {EmployeeModel} from '../../../../@core/models/employee.model';
import {AccountService} from '../../../../@core/services/account.service';
import {EmployeeService} from '../../../../@core/services/employee.service';

@Component({
    selector: 'vacation-set',
    templateUrl: './vacation-set.component.html',
    styleUrls: ['./vacation-set.component.scss']
})
export class VacationSetComponent implements OnInit {
    employees: EmployeeModel[] = [];
    selectedEmployee: EmployeeModel;

    // source: LocalDataSource = new LocalDataSource();
    loadingVacation: boolean = false;


    constructor(private accountService: AccountService,
                private employeeService: EmployeeService) {
    }

    ngOnInit(): void {
        this.employeeService.getCurrentEmployee()
            .subscribe(employee => {
                this.selectedEmployee = employee;
            });
    }


    // edit(event: TableEventEditModel<>) {
    //     // this.queueService.updateQueueRequest(this.targetId, event.data.id, event.newData)
    //     //     .subscribe(event.confirm.resolve);
    // }
    //
    // delete(event: TableEventDeleteModel<>) {
    //     // this.queueService.deleteQueueRequest(this.targetId, event.data.id)
    //     //     .subscribe(response => event.confirm.resolve());
    // }
    //
    // create(event: TableEventAddModel<>) {
    //     // this.queueService.addQueueRequest(this.targetId, event.newData)
    //     //     .subscribe(event.confirm.resolve);
    // }

}

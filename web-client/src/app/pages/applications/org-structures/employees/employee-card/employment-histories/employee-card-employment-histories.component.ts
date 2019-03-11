import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {CustomTableComponent} from "../../../../../../@theme/components";
import {Column} from "ng2-smart-table/lib/data-set/column";
import {Cell} from "ng2-smart-table";
import {Utils} from "../../../../../../@core/utils/utils";
import {EmployeeCardEmploymentHistoriesDataSource} from "./employee-card-employment-histories.data-source";
import {EmploymentHistoryModel} from "../../../../../../@core/models/employee/employment-history.model";
import {Subject} from "rxjs";
import {EmployeeEmploymentHistoriesService} from "./employee-employment-histories.service";
import {EmployeeCardService} from "../employee-card.service";

@Component({
    selector: 'employee-card-employment-histories',
    templateUrl: './employee-card-employment-histories.component.html',
    styleUrls: ['./employee-card-employment-histories.component.scss']
})
export class EmployeeCardEmploymentHistoriesComponent implements OnInit, OnDestroy {
    private readonly destroySubjects: Subject<any> = new Subject<any>();

    @ViewChild('employmentHistoryTable')
    employmentHistoryTable: CustomTableComponent;

    employmentHistoryColumns: Column[] = [];
    employmentHistoryDataSource: EmployeeCardEmploymentHistoriesDataSource;

    constructor(private employeeCardService: EmployeeCardService,
                private employeeEmploymentHistoriesService: EmployeeEmploymentHistoriesService) {
        this.employmentHistoryDataSource =
            new EmployeeCardEmploymentHistoriesDataSource(this.employeeCardService, this.employeeEmploymentHistoriesService);
    }

    ngOnInit(): void {
        const organizationColumn: Column = new Column('organization', {
            title: 'Организация',
            type: 'string',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
        }, null);

        const postColumn: Column = new Column('post', {
            title: 'Должность',
            type: 'string',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
        }, null);

        const startWorkDateColumn: Column = new Column('startWorkDate', {
            title: 'Дата начала работы',
            type: 'string',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
            valuePrepareFunction(a: any, value: EmploymentHistoryModel, cell: Cell) {
                return Utils.getDateFormat(value.startWorkDate);
            }
        }, null);

        const endWorkDateColumn: Column = new Column('endWorkDate', {
            title: 'Дата окончания работы',
            type: 'string',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
            valuePrepareFunction(a: any, value: EmploymentHistoryModel, cell: Cell) {
                return Utils.getDateFormat(value.endWorkDate);
            }
        }, null);

        this.employmentHistoryColumns.push(
            organizationColumn,
            postColumn,
            startWorkDateColumn,
            endWorkDateColumn
        );
    }

    ngOnDestroy() {
        this.destroySubjects.next(true);
        this.destroySubjects.complete();
    }

}

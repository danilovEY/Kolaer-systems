import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {CustomTableComponent} from "../../../../../../@theme/components";
import {Column} from "ng2-smart-table/lib/data-set/column";
import {Cell} from "ng2-smart-table";
import {Utils} from "../../../../../../@core/utils/utils";
import {EmployeeCardCombinationsDataSource} from "./employee-card-combinations.data-source";
import {EmployeeCombinationModel} from "../../../../../../@core/models/employee/employee-combination.model";
import {EmployeeCombinationsService} from "./employee-combinations.service";
import {Subject} from "rxjs";
import {EmployeeCardService} from "../employee-card.service";

@Component({
    selector: 'employee-card-combinations',
    templateUrl: './employee-card-combinations.component.html',
    styleUrls: ['./employee-card-combinations.component.scss']
})
export class EmployeeCardCombinationsComponent implements OnInit, OnDestroy {
    private readonly destroySubjects: Subject<any> = new Subject<any>();

    @ViewChild('combinationTable')
    combinationTable: CustomTableComponent;

    combinationColumns: Column[] = [];
    combinationDataSource: EmployeeCardCombinationsDataSource;

    constructor(private employeeCardService: EmployeeCardService,
                private employeeCombinationService: EmployeeCombinationsService) {
        this.combinationDataSource =
            new EmployeeCardCombinationsDataSource(this.employeeCardService, this.employeeCombinationService);
    }

    ngOnInit(): void {
        const postColumn: Column = new Column('post', {
            title: 'Должность зам. работника',
            type: 'string',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
        }, null);

        const startDateColumn: Column = new Column('startDate', {
            title: 'Действует с',
            type: 'string',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
            valuePrepareFunction(a: any, value: EmployeeCombinationModel, cell: Cell) {
                return Utils.getDateFormat(value.startDate);
            }
        }, null);

        const endDateColumn: Column = new Column('endDate', {
            title: 'Действует по',
            type: 'string',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
            valuePrepareFunction(a: any, value: EmployeeCombinationModel, cell: Cell) {
                return Utils.getDateFormat(value.startDate);
            }
        }, null);

        this.combinationColumns.push(
            postColumn,
            startDateColumn,
            endDateColumn
        );
    }

    ngOnDestroy() {
        this.destroySubjects.next(true);
        this.destroySubjects.complete();
    }

}

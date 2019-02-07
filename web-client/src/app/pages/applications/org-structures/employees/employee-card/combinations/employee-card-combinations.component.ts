import {Component, OnInit, ViewChild} from '@angular/core';
import {CustomTableComponent} from "../../../../../../@theme/components";
import {Column} from "ng2-smart-table/lib/data-set/column";
import {Cell} from "ng2-smart-table";
import {Utils} from "../../../../../../@core/utils/utils";
import {EmployeeCardCombinationsDataSource} from "./employee-card-combinations.data-source";
import {EmployeeCombinationModel} from "../../../../../../@core/models/employee/employee-combination.model";

@Component({
    selector: 'employee-card-combinations',
    templateUrl: './employee-card-combinations.component.html',
    styleUrls: ['./employee-card-combinations.component.scss']
})
export class EmployeeCardCombinationsComponent implements OnInit {

    @ViewChild('combinationTable')
    combinationTable: CustomTableComponent;

    combinationColumns: Column[] = [];
    combinationDataSource: EmployeeCardCombinationsDataSource = new EmployeeCardCombinationsDataSource();

    constructor() {

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

}

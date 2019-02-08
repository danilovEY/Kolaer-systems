import {Component, OnInit, ViewChild} from '@angular/core';
import {CustomTableComponent} from "../../../../../../@theme/components";
import {Column} from "ng2-smart-table/lib/data-set/column";
import {Cell} from "ng2-smart-table";
import {Utils} from "../../../../../../@core/utils/utils";
import {EmployeeCardRelativesDataSource} from "./employee-card-relatives.data-source";
import {EmployeeRelativeModel} from "../../../../../../@core/models/employee/employee-relative.model";

@Component({
    selector: 'employee-card-relatives',
    templateUrl: './employee-card-relatives.component.html',
    styleUrls: ['./employee-card-relatives.component.scss']
})
export class EmployeeCardRelativesComponent implements OnInit {

    @ViewChild('relativeTable')
    relativeTable: CustomTableComponent;

    relativeColumns: Column[] = [];
    relativeDataSource: EmployeeCardRelativesDataSource = new EmployeeCardRelativesDataSource();

    constructor() {

    }

    ngOnInit(): void {
        const relationDegreeColumn: Column = new Column('relationDegree', {
            title: 'Степень родства',
            type: 'string',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
        }, null);

        const initialsColumn: Column = new Column('initials', {
            title: 'ФИО',
            type: 'string',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
        }, null);

        const dateOfBirthColumn: Column = new Column('dateOfBirth', {
            title: 'Дата рождения',
            type: 'string',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
            valuePrepareFunction(a: any, value: EmployeeRelativeModel, cell: Cell) {
                return Utils.getDateFormat(value.dateOfBirth);
            }
        }, null);

        this.relativeColumns.push(
            relationDegreeColumn,
            initialsColumn,
            dateOfBirthColumn
        );
    }

}

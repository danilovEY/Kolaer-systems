import {Component, OnInit, ViewChild} from '@angular/core';
import {CustomTableComponent} from "../../../../../../@theme/components";
import {EmployeeCardEducationDataSource} from "./employee-card-education.data-source";
import {Column} from "ng2-smart-table/lib/data-set/column";
import {Cell} from "ng2-smart-table";
import {Utils} from "../../../../../../@core/utils/utils";
import {EmployeeEducationModel} from "../../../../../../@core/models/employee/employee-education.model";

@Component({
    selector: 'employee-card-educations',
    templateUrl: './employee-card-educations.component.html',
    styleUrls: ['./employee-card-educations.component.scss']
})
export class EmployeeCardEducationsComponent implements OnInit {

    @ViewChild('educationTable')
    educationTable: CustomTableComponent;

    educationColumns: Column[] = [];
    educationDataSource: EmployeeCardEducationDataSource = new EmployeeCardEducationDataSource();

    constructor() {

    }

    ngOnInit(): void {
        const typeEducationColumn: Column = new Column('typeEducation', {
            title: 'Вид образования',
            type: 'string',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
        }, null);

        const institutionColumn: Column = new Column('institution', {
            title: 'Учебное заведение',
            type: 'string',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
        }, null);

        const specialtyColumn: Column = new Column('specialty', {
            title: 'Специальность',
            type: 'string',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
        }, null);

        const qualificationColumn: Column = new Column('qualification', {
            title: 'Квалицикация',
            type: 'string',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
        }, null);

        const documentColumn: Column = new Column('document', {
            title: 'Документ',
            type: 'string',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
        }, null);

        const documentNumberColumn: Column = new Column('documentNumber', {
            title: 'Номер документа',
            type: 'string',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
        }, null);

        const expirationDateColumn: Column = new Column('expirationDate', {
            title: 'Дата окончания',
            type: 'string',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
            valuePrepareFunction(a: any, value: EmployeeEducationModel, cell: Cell) {
                return Utils.getDateFormat(value.expirationDate);
            }
        }, null);

        this.educationColumns.push(
            typeEducationColumn,
            institutionColumn,
            specialtyColumn,
            qualificationColumn,
            documentColumn,
            documentNumberColumn,
            expirationDateColumn
        );
    }

}

import {Component, OnInit, ViewChild} from '@angular/core';
import {CustomTableComponent} from "../../../../../../@theme/components";
import {Column} from "ng2-smart-table/lib/data-set/column";
import {Cell} from "ng2-smart-table";
import {Utils} from "../../../../../../@core/utils/utils";
import {EmployeeCardPersonalDocumentsDataSource} from "./employee-card-personal-documents.data-source";
import {EmployeePersonalDocumentModel} from "../../../../../../@core/models/employee/employee-personal-document.model";

@Component({
    selector: 'employee-card-personal-documents',
    templateUrl: './employee-card-personal-documents.component.html',
    styleUrls: ['./employee-card-personal-documents.component.scss']
})
export class EmployeeCardPersonalDocumentsComponent implements OnInit {

    @ViewChild('personalDocumentTable')
    personalDocumentTable: CustomTableComponent;

    personalDocumentColumns: Column[] = [];
    personalDocumentDataSource: EmployeeCardPersonalDocumentsDataSource = new EmployeeCardPersonalDocumentsDataSource();

    constructor() {

    }

    ngOnInit(): void {
        const nameColumn: Column = new Column('name', {
            title: 'Наименование',
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

        const dateOfIssueColumn: Column = new Column('dateOfIssue', {
            title: 'Дата выдачи',
            type: 'string',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
            valuePrepareFunction(a: any, value: EmployeePersonalDocumentModel, cell: Cell) {
                return Utils.getDateFormat(value.dateOfIssue);
            }
        }, null);

        const issuedByColumn: Column = new Column('issuedBy', {
            title: 'Кем выдан',
            type: 'string',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
        }, null);

        this.personalDocumentColumns.push(
            nameColumn,
            documentNumberColumn,
            dateOfIssueColumn,
            issuedByColumn
        );
    }

}

import {Component, OnInit, ViewChild} from '@angular/core';
import {CustomTableComponent} from "../../../../../../@theme/components";
import {Column} from "ng2-smart-table/lib/data-set/column";
import {Cell} from "ng2-smart-table";
import {Utils} from "../../../../../../@core/utils/utils";
import {EmployeeCardVacationsDataSource} from "./employee-card-vacations.data-source";
import {VacationModel} from "../../../../vacation/model/vacation.model";
import {EmployeeCardService} from "../employee-card.service";
import {VacationService} from "../../../../vacation/vacation.service";

@Component({
    selector: 'employee-card-vacations',
    templateUrl: './employee-card-vacations.component.html',
    styleUrls: ['./employee-card-vacations.component.scss']
})
export class EmployeeCardVacationsComponent implements OnInit {

    @ViewChild('vacationTable')
    vacationTable: CustomTableComponent;

    vacationColumns: Column[] = [];
    vacationDataSource: EmployeeCardVacationsDataSource = new EmployeeCardVacationsDataSource();

    constructor(private employeeCardService: EmployeeCardService,
                private vacationService: VacationService) {

    }

    ngOnInit(): void {
        const noteColumn: Column = new Column('note', {
            title: 'Примечание',
            type: 'string',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
        }, null);

        const vacationFromColumn: Column = new Column('vacationFrom', {
            title: 'Дата начала',
            type: 'string',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
            valuePrepareFunction(a: any, value: VacationModel, cell: Cell) {
                return Utils.getDateFormat(value.vacationFrom);
            }
        }, null);

        const vacationToColumn: Column = new Column('vacationTo', {
            title: 'Дата окончания',
            type: 'string',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
            valuePrepareFunction(a: any, value: VacationModel, cell: Cell) {
                return Utils.getDateFormat(value.vacationTo);
            }
        }, null);

        const vacationDaysColumn: Column = new Column('vacationDays', {
            title: 'Кол-во дней',
            type: 'number',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
        }, null);

        this.vacationColumns.push(
            vacationFromColumn,
            vacationToColumn,
            vacationDaysColumn,
            noteColumn
        );
    }

}

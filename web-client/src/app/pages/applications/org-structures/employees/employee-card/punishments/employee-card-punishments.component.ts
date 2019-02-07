import {Component, OnInit, ViewChild} from '@angular/core';
import {CustomTableComponent} from "../../../../../../@theme/components";
import {Column} from "ng2-smart-table/lib/data-set/column";
import {Cell} from "ng2-smart-table";
import {Utils} from "../../../../../../@core/utils/utils";
import {EmployeeCardPunishmentsDataSource} from "./employee-card-punishments.data-source";
import {EmployeeAchievementModel} from "../../../../../../@core/models/employee/employee-achievement.model";

@Component({
    selector: 'employee-card-punishments',
    templateUrl: './employee-card-punishments.component.html',
    styleUrls: ['./employee-card-punishments.component.scss']
})
export class EmployeeCardPunishmentsComponent implements OnInit {

    @ViewChild('punishmentTable')
    punishmentTable: CustomTableComponent;

    punishmentColumns: Column[] = [];
    punishmentDataSource: EmployeeCardPunishmentsDataSource = new EmployeeCardPunishmentsDataSource();

    constructor() {

    }

    ngOnInit(): void {
        const nameColumn: Column = new Column('name', {
            title: 'Вид взыскания',
            type: 'string',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
        }, null);

        const statusColumn: Column = new Column('status', {
            title: 'Статус',
            type: 'string',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
        }, null);

        const startPunishmentDate: Column = new Column('startPunishmentDate', {
            title: 'Дата вступления в действие',
            type: 'string',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
            valuePrepareFunction(a: any, value: EmployeeAchievementModel, cell: Cell) {
                return Utils.getDateFormat(value.orderDate);
            }
        }, null);

        const orderNumberColumn: Column = new Column('orderNumber', {
            title: 'Номер приказа',
            type: 'string',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
        }, null);

        const orderDateColumn: Column = new Column('orderDate', {
            title: 'Дата приказа',
            type: 'string',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
            valuePrepareFunction(a: any, value: EmployeeAchievementModel, cell: Cell) {
                return Utils.getDateFormat(value.orderDate);
            }
        }, null);

        this.punishmentColumns.push(
            nameColumn,
            statusColumn,
            startPunishmentDate,
            orderNumberColumn,
            orderDateColumn
        );
    }

}

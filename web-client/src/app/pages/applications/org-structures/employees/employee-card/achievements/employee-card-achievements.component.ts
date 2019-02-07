import {Component, OnInit, ViewChild} from '@angular/core';
import {CustomTableComponent} from "../../../../../../@theme/components";
import {Column} from "ng2-smart-table/lib/data-set/column";
import {Cell} from "ng2-smart-table";
import {Utils} from "../../../../../../@core/utils/utils";
import {EmployeeCardAchievementsDataSource} from "./employee-card-achievements.data-source";
import {EmployeeAchievementModel} from "../../../../../../@core/models/employee/employee-achievement.model";

@Component({
    selector: 'employee-card-achievements',
    templateUrl: './employee-card-achievements.component.html',
    styleUrls: ['./employee-card-achievements.component.scss']
})
export class EmployeeCardAchievementsComponent implements OnInit {

    @ViewChild('achievementTable')
    achievementTable: CustomTableComponent;

    achievementColumns: Column[] = [];
    achievementDataSource: EmployeeCardAchievementsDataSource = new EmployeeCardAchievementsDataSource();

    constructor() {

    }

    ngOnInit(): void {
        const nameColumn: Column = new Column('name', {
            title: 'Вид достижения',
            type: 'string',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
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

        this.achievementColumns.push(
            nameColumn,
            orderNumberColumn,
            orderDateColumn
        );
    }

}

import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {CustomTableComponent} from "../../../../../../@theme/components";
import {Column} from "ng2-smart-table/lib/data-set/column";
import {Cell} from "ng2-smart-table";
import {Utils} from "../../../../../../@core/utils/utils";
import {EmployeeCardAchievementsDataSource} from "./employee-card-achievements.data-source";
import {EmployeeAchievementModel} from "../../../../../../@core/models/employee/employee-achievement.model";
import {EmployeeAchievementService} from "./employee-achievement.service";
import {Subject} from "rxjs";
import {EmployeeCardService} from "../employee-card.service";

@Component({
    selector: 'employee-card-achievements',
    templateUrl: './employee-card-achievements.component.html',
    styleUrls: ['./employee-card-achievements.component.scss']
})
export class EmployeeCardAchievementsComponent implements OnInit, OnDestroy {
    private readonly destroySubjects: Subject<any> = new Subject<any>();

    @ViewChild('achievementTable')
    achievementTable: CustomTableComponent;

    achievementColumns: Column[] = [];
    achievementDataSource: EmployeeCardAchievementsDataSource;

    constructor(private employeeCardService: EmployeeCardService,
                private employeeAchievementService: EmployeeAchievementService) {
        this.achievementDataSource = new EmployeeCardAchievementsDataSource(employeeCardService, employeeAchievementService);
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

    ngOnDestroy() {
        this.destroySubjects.next(true);
        this.destroySubjects.complete();
    }

}

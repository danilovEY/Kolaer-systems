import {Component, OnInit, ViewChild} from '@angular/core';
import {CustomTableComponent} from "../../../../../../@theme/components";
import {Column} from "ng2-smart-table/lib/data-set/column";
import {Cell} from "ng2-smart-table";
import {Utils} from "../../../../../../@core/utils/utils";
import {EmployeeCardStaffMovementsDataSource} from "./employee-card-staff-movements.data-source"
import {EmployeeStaffMovementModel} from "../../../../../../@core/models/employee/employee-staff-movement.model";
import {EmployeeCardService} from "../employee-card.service";
import {EmployeeStaffMovementsService} from "./employee-staff-movements.service";

@Component({
    selector: 'employee-card-staff-movements',
    templateUrl: './employee-card-staff-movements.component.html',
    styleUrls: ['./employee-card-staff-movements.component.scss']
})
export class EmployeeCardStaffMovementsComponent implements OnInit {

    @ViewChild('staffMovementHistoryTable')
    staffMovementTable: CustomTableComponent;

    staffMovementColumns: Column[] = [];
    staffMovementDataSource: EmployeeCardStaffMovementsDataSource;

    constructor(private employeeCardService: EmployeeCardService,
                private employeeStaffMovementsService: EmployeeStaffMovementsService) {
        this.staffMovementDataSource =
            new EmployeeCardStaffMovementsDataSource(this.employeeCardService, this.employeeStaffMovementsService);
    }

    ngOnInit(): void {
        const startWorkDate: Column = new Column('startWorkDate', {
            title: 'Дата начала работы',
            type: 'string',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
            valuePrepareFunction(a: any, value: EmployeeStaffMovementModel, cell: Cell) {
                return Utils.getDateFormat(value.startWorkDate);
            }
        }, null);

        const endWorkDate: Column = new Column('endWorkDate', {
            title: 'Дата окончания работы',
            type: 'string',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
            valuePrepareFunction(a: any, value: EmployeeStaffMovementModel, cell: Cell) {
                return Utils.getDateFormat(value.endWorkDate);
            }
        }, null);

        const nameColumn: Column = new Column('name', {
            title: 'Вид мероприятия',
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

        const departmentColumn: Column = new Column('department', {
            title: 'Подразделение',
            type: 'string',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
        }, null);

        const categoryUnitColumn: Column = new Column('categoryUnit', {
            title: 'Категория',
            type: 'string',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
        }, null);

        const salaryColumn: Column = new Column('salary', {
            title: 'Оклад',
            type: 'string',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
        }, null);

        const surchargeHarmfulnessColumn: Column = new Column('surchargeHarmfulness', {
            title: 'Доплата за вредность, %',
            type: 'string',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
        }, null);

        const cardSlamColumn: Column = new Column('cardSlam', {
            title: 'Карта СОУТ',
            type: 'string',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
        }, null);

        const classWorkingConditionsColumn: Column = new Column('classWorkingConditions', {
            title: 'Класс условий труда',
            type: 'string',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
        }, null);

        const subClassWorkingConditionsColumn: Column = new Column('subClassWorkingConditions', {
            title: 'Подкласс условий труда',
            type: 'string',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
        }, null);

        const orderNumber: Column = new Column('orderNumber', {
            title: 'Номер приказа',
            type: 'string',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
        }, null);

        const orderDate: Column = new Column('orderDate', {
            title: 'Дата приказа',
            type: 'string',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
            valuePrepareFunction(a: any, value: EmployeeStaffMovementModel, cell: Cell) {
                return Utils.getDateFormat(value.orderDate);
            }
        }, null);

        this.staffMovementColumns.push(
            startWorkDate,
            endWorkDate,
            nameColumn,
            postColumn,
            departmentColumn,
            categoryUnitColumn,
            salaryColumn,
            surchargeHarmfulnessColumn,
            cardSlamColumn,
            classWorkingConditionsColumn,
            subClassWorkingConditionsColumn,
            orderNumber,
            orderDate
        );
    }

}

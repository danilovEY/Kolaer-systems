import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {CustomTableComponent} from "../../../../../../@theme/components";
import {Column} from "ng2-smart-table/lib/data-set/column";
import {Cell} from "ng2-smart-table";
import {Utils} from "../../../../../../@core/utils/utils";
import {EmployeeCardPersonalDataDataSource} from "./employee-card-personal-data.data-source";
import {EmployeePersonalDataModel} from "../../../../../../@core/models/employee/employee-personal-data.model";
import {EmployeePersonalDataService} from "./employee-personal-data.service";
import {Subject} from "rxjs";
import {EmployeeCardService} from "../employee-card.service";

@Component({
    selector: 'employee-card-personal-data',
    templateUrl: './employee-card-personal-data.component.html',
    styleUrls: ['./employee-card-personal-data.component.scss']
})
export class EmployeeCardPersonalDataComponent implements OnInit, OnDestroy {
    private readonly destroySubjects: Subject<any> = new Subject<any>();

    @ViewChild('personalDataTable')
    personalDataTable: CustomTableComponent;

    personalDataColumns: Column[] = [];
    personalDataDataSource: EmployeeCardPersonalDataDataSource;

    constructor(private employeeCardService: EmployeeCardService,
                private employeePersonalDataService: EmployeePersonalDataService) {
        this.personalDataDataSource =
            new EmployeeCardPersonalDataDataSource(this.employeeCardService, this.employeePersonalDataService);
    }


    ngOnInit(): void {
        const maritalStatusColumn: Column = new Column('maritalStatus', {
            title: 'Семейное положение',
            type: 'string',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
        }, null);

        const phoneNumberColumn: Column = new Column('phoneNumber', {
            title: 'Номер телефона',
            type: 'string',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
        }, null);

        const addressRegistrationColumn: Column = new Column('addressRegistration', {
            title: 'Адрес регистрации по месту жительства',
            type: 'string',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
        }, null);

        const addressResidentialColumn: Column = new Column('addressResidential', {
            title: 'Адрес фактического проживания',
            type: 'string',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
        }, null);

        const placeOfBirthColumn: Column = new Column('placeOfBirth', {
            title: 'Место рождения',
            type: 'string',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
        }, null);

        const disabilityGroupColumn: Column = new Column('disabilityGroup', {
            title: 'Группа инвалидности',
            type: 'string',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
        }, null);

        const disabilityDateColumn: Column = new Column('disabilityDate', {
            title: 'Дата приказа',
            type: 'string',
            editable: false,
            addable: false,
            filter: false,
            sort: false,
            valuePrepareFunction(a: any, value: EmployeePersonalDataModel, cell: Cell) {
                return Utils.getDateFormat(value.disabilityDate);
            }
        }, null);

        this.personalDataColumns.push(
            maritalStatusColumn,
            phoneNumberColumn,
            addressRegistrationColumn,
            addressResidentialColumn,
            placeOfBirthColumn,
            disabilityGroupColumn,
            disabilityDateColumn
        );
    }

    ngOnDestroy() {
        this.destroySubjects.next(true);
        this.destroySubjects.complete();
    }

}

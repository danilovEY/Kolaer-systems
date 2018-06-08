import {Component, OnInit, ViewChild} from '@angular/core';
import {Column} from 'ng2-smart-table/lib/data-set/column';
import {CustomTableComponent} from '../../../../@theme/components';
import {TableEventAddModel} from '../../../../@theme/components/table/table-event-add.model';
import {TableEventDeleteModel} from '../../../../@theme/components/table/table-event-delete.model';
import {TableEventEditModel} from '../../../../@theme/components/table/table-event-edit.model';
import {DepartmentsDataSource} from './departments.data-source';
import {DepartmentService} from '../../../../@core/services/department.service';
import {DepartmentModel} from '../../../../@core/models/department.model';

@Component({
    selector: 'departments',
    styleUrls: ['./departments.component.scss'],
    templateUrl: './departments.component.html'
})
export class DepartmentsComponent implements OnInit {
    @ViewChild('departmentsTable')
    departmentsTable: CustomTableComponent;

    departmentsColumns: Column[] = [];
    departmentsSource: DepartmentsDataSource;
    departmentsLoading: boolean = true;

    constructor(private departmentService: DepartmentService) {
        this.departmentsSource = new DepartmentsDataSource(this.departmentService);
        this.departmentsSource.onLoading().subscribe(load => this.departmentsLoading = load);
    }

    ngOnInit() {
        const nameColumn: Column = new Column('name', {
            title: 'Имя',
            type: 'string'
        }, null);

        const abbreviatedNameColumn: Column = new Column('abbreviatedName', {
            title: 'Сокращение',
            type: 'string'
        }, null);



        this.departmentsColumns.push(abbreviatedNameColumn, nameColumn);
    }

    departmentsEdit(event: TableEventEditModel<DepartmentModel>) {

    }

    departmentsCreate(event: TableEventAddModel<DepartmentModel>) {

    }

    departmentsDelete(event: TableEventDeleteModel<DepartmentModel>) {

    }
}

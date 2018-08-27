import {Component, ViewChild} from '@angular/core';
import {Column} from 'ng2-smart-table/lib/data-set/column';
import {CustomTableComponent} from '../../../../@theme/components';
import {TypeWorkDataSource} from './type-work.data-source';
import {TypeWorkService} from '../../../../@core/services/type-work.service';
import {TableEventAddModel} from '../../../../@theme/components/table/table-event-add.model';
import {TableEventEditModel} from '../../../../@theme/components/table/table-event-edit.model';
import {TableEventDeleteModel} from '../../../../@theme/components/table/table-event-delete.model';
import {TypeWorkModel} from '../../../../@core/models/type-work.model';

@Component({
    selector: 'type-work-org-structure',
    templateUrl: './type-work.component.html',
    styleUrls: ['./type-work.component.html']
})
export class TypeWorkComponent {
    @ViewChild('typeWorkTable')
    typeWorkTable: CustomTableComponent;

    typeWorkColumns: Column[] = [];
    typeWorkSource: TypeWorkDataSource;
    typeWorkLoading: boolean = true;

    constructor(private typeWorkService: TypeWorkService) {
        this.typeWorkSource = new TypeWorkDataSource(this.typeWorkService);
        this.typeWorkSource.onLoading().subscribe(load => this.typeWorkLoading = load);
    }

    ngOnInit() {
        const nameColumn: Column = new Column('name', {
            title: 'Имя',
            type: 'string',
            filter: false,
            sort: false,
        }, null);

        this.typeWorkColumns.push(nameColumn);
    }

    typeWorkEdit(event: TableEventEditModel<TypeWorkModel>) {
        this.typeWorkService.update(event.data.id, event.newData)
            .subscribe((employee: TypeWorkModel) => event.confirm.resolve(event.newData, employee),
                error2 => event.confirm.reject({}));
    }

    typeWorkCreate(event: TableEventAddModel<TypeWorkModel>) {
        this.typeWorkService.add(event.newData)
            .subscribe((employee: TypeWorkModel) => event.confirm.resolve(employee),
                error2 => event.confirm.reject({}));
    }

    typeWorkDelete(event: TableEventDeleteModel<TypeWorkModel>) {
        if (confirm(`Вы действительно хотите удалить: ${event.data.name}?`)) {
            this.typeWorkService.delete(event.data.id)
                .subscribe(response => event.confirm.resolve(),
                    error2 => event.confirm.reject({}));
        } else {
            event.confirm.reject({});
        }
    }
}

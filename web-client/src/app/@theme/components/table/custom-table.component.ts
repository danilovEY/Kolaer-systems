import {Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {DataSource} from 'ng2-smart-table/lib/data-source/data-source';
import {CustomActionEditComponent} from './custom-action-edit.component';
import {CustomActionViewComponent} from './custom-action-view.component';
import {Column} from 'ng2-smart-table/lib/data-set/column';
import {Ng2SmartTableComponent} from 'ng2-smart-table/ng2-smart-table.component';
import {CustomActionModel} from './custom-action.model';

@Component({
    selector: 'custom-table',
    template: `
        <ng2-smart-table #table [settings]="settings" [source]="source"></ng2-smart-table>
    `
})
export class CustomTableComponent implements OnInit {
    @Input() source: DataSource;
    @Input() actionAdd: boolean = true;
    @Input() actionEdit: boolean = true;
    @Input() actionDelete: boolean = true;
    @Input() actionWidth: string = '30px';

    @Input() columns: Column[] = [];
    @Input() actions: CustomActionModel[] = [];

    @Output() action = new EventEmitter<any>();
    @Output() actionBeforeValueView: Function;

    @ViewChild('table')
    table: Ng2SmartTableComponent;

    settings = {
        hideSubHeader: false,
        actions: {
            columnTitle: 'Действия',
            add: this.actionAdd,
            edit: this.actionEdit,
            delete: this.actionDelete,
            position: 'right',
        },
        pager: {
            display: true,
            perPage: 15,
        },
        noDataMessage: 'Пусто',
        selectMode: 'single',
        add: {
            addButtonContent: '<i class="nb-plus"></i>',
            createButtonContent: '<i class="nb-checkmark"></i>',
            cancelButtonContent: '<i class="nb-close"></i>',
        },
        edit: {
            editButtonContent: '<i class="nb-edit"></i>',
            saveButtonContent: '<i class="nb-checkmark"></i>',
            cancelButtonContent: '<i class="nb-close"></i>',
        },
        delete: {
            deleteButtonContent: '<i class="nb-trash"></i>',
            confirmDelete: true,
        },
        columns:  {},
    };

    ngOnInit() {
        this.settings.actions.add = this.actionAdd;
        this.settings.actions.edit = this.actionEdit;
        this.settings.actions.delete = this.actionDelete;

        if (this.actions.length > 0) {
            const tempActions = this.actions;
            const tempAction = this.action;
            const tempActionBeforeValueView = this.actionBeforeValueView;
            const customActions: Column = new Column('customActions', {
                title: 'Прочие действия',
                type: 'custom',
                editable: false,
                addable: false,
                filter: false,
                sort: false,
                width: this.actionWidth,
                editor: {
                    type: 'custom',
                    component: CustomActionEditComponent,
                },
                renderComponent: CustomActionViewComponent,
                onComponentInitFunction(instance: CustomActionViewComponent) {
                    instance.actions = tempActions;
                    instance.custom = tempAction;
                    instance.actionBeforeValueView = tempActionBeforeValueView;
                }
            }, undefined);

            this.columns.push(customActions);
        }

        for (const col of this.columns) {
            this.settings.columns[col.id] = col;
        }
    }
}

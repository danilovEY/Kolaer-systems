import {Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {DataSource} from 'ng2-smart-table/lib/data-source/data-source';
import {CustomActionEditComponent} from './custom-action-edit.component';
import {CustomActionViewComponent} from './custom-action-view.component';
import {Column} from 'ng2-smart-table/lib/data-set/column';
import {Ng2SmartTableComponent} from 'ng2-smart-table/ng2-smart-table.component';
import {CustomActionModel} from './custom-action.model';
import {TableEventDeleteModel} from './table-event-delete.model';
import {TableEventEditModel} from './table-event-edit.model';
import {TableEventAddModel} from './table-event-add.model';
import {Row} from 'ng2-smart-table/lib/data-set/row';

@Component({
    selector: 'custom-table',
    template: `
        
        <ng-container *ngIf="actionAdd">
            <div class="text-right">
                <button class="btn btn-success btn-semi-round btn-demo" (click)="addNewRow()">Добавить запись</button>
            </div>
            
            <br/>
        </ng-container>
        
        <ng2-smart-table #table [settings]="settings" [source]="source" 
                         (delete)="deleteConfirm($event)"
                         (create)="createConfirm($event)"
                         (edit)="editConfirm($event)"
                         (deleteConfirm)="deleteConfirm($event)"
                         (createConfirm)="createConfirm($event)"
                         (editConfirm)="editConfirm($event)">
        </ng2-smart-table>
    `
})
export class CustomTableComponent implements OnInit {
    public static readonly DELETE_ACTION_NAME: string = 'delete';
    public static readonly EDIT_ACTION_NAME: string = 'edit';

    public selectedRow: Row;
    public columnActions: Column;

    private readonly defaultAction = new EventEmitter<any>();

    private editAction: CustomActionModel;
    private deleteAction: CustomActionModel;

    @Input() source: DataSource;
    @Input() actionAdd: boolean = true;
    @Input() actionDelete: boolean = true;
    @Input() actionEdit: boolean = true;
    @Input() actionExternal: boolean = false;

    @Input() columns: Column[] = [];
    @Input() actions: CustomActionModel[] = [];

    @Output() action = new EventEmitter<any>();
    actionBeforeValueView: Function;

    @Output() delete = new EventEmitter<any>();
    @Output() create = new EventEmitter<any>();
    @Output() edit = new EventEmitter<any>();

    @ViewChild('table')
    table: Ng2SmartTableComponent;

    settings: any = {
        hideSubHeader: false,
        mode: 'inline',
        actions: {
            columnTitle: 'Действия',
            add: false,
            edit: false,
            delete: false,
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
            confirmCreate: true,
        },
        edit: {
            editButtonContent: '<i class="nb-edit"></i>',
            saveButtonContent: '<i class="nb-checkmark"></i>',
            cancelButtonContent: '<i class="nb-close"></i>',
            confirmSave: true,
        },
        delete: {
            deleteButtonContent: '<i class="nb-trash"></i>',
            confirmDelete: true,
        },
        columns:  {
        }
    };

    ngOnInit() {
        if (this.actionExternal) {
            this.settings.mode = 'external';
        }

        this.editAction = new CustomActionModel();
        this.editAction.name = CustomTableComponent.EDIT_ACTION_NAME;
        this.editAction.content = '<i class="fa fa-edit"></i>';
        this.editAction.description = 'Редактировать';

        this.deleteAction = new CustomActionModel();
        this.deleteAction.name = CustomTableComponent.DELETE_ACTION_NAME;
        this.deleteAction.content = '<i class="fa fa-trash"></i>';
        this.deleteAction.description = 'Удалить';

        if (this.actionEdit) {
            this.actions.push(this.editAction);
        }

        if (this.actionDelete) {
            this.actions.push(this.deleteAction);
        }


        if (this.actions.length > 0) {
            this.defaultAction.subscribe(event => {
                if (event.action.name === CustomTableComponent.EDIT_ACTION_NAME) {
                    if (this.actionExternal) {
                        const tableEvent = new TableEventEditModel();
                        tableEvent.source = this.source;
                        tableEvent.data = this.selectedRow.getData();
                        tableEvent.newData = this.selectedRow.getData();

                        this.editConfirm(tableEvent);
                    } else {
                        this.table.grid.edit(this.selectedRow);
                    }
                } if (event.action.name === CustomTableComponent.DELETE_ACTION_NAME) {
                    if (this.actionExternal) {
                        const tableEvent = new TableEventDeleteModel();
                        tableEvent.source = this.source;
                        tableEvent.data = this.selectedRow.getData();

                        this.deleteConfirm(tableEvent);
                    } else {
                        this.table.grid.delete(this.selectedRow, this.table.deleteConfirm);
                    }
                }

                if (this.action) {
                    this.action.emit(event);
                }
            });

            const tempActions = this.actions;
            const tempAction = this.defaultAction;
            const tempActionBeforeValueView = this.actionBeforeValueView;

            this.columnActions = new Column('customActions', {
                title: 'Действия',
                type: 'custom',
                editable: false,
                addable: false,
                filter: false,
                sort: false,
                editor: {
                    type: 'custom',
                    config: {
                        getGrid: () => this.table.grid // BAD API table
                    },
                    component: CustomActionEditComponent,
                },
                renderComponent: CustomActionViewComponent,
                onComponentInitFunction(instance: CustomActionViewComponent) {
                    instance.actions = tempActions;
                    instance.custom = tempAction;
                    instance.actionBeforeValueView = tempActionBeforeValueView;
                }
            }, undefined);

            this.columns.push(this.columnActions);
        }

        for (const col of this.columns) {
            this.settings.columns[col.id] = col;
            this.settings.columns[col.id].editable = col.isEditable; // BAD API table
            this.settings.columns[col.id].addable = col.isAddable;   //
            this.settings.columns[col.id].filter = col.isFilterable ? col.filter : false; //
            this.settings.columns[col.id].sort = col.isSortable;     //
        }

        this.table.rowHover.subscribe(row => this.selectedRow = row);
    }

    addNewRow() {
        if (this.actionExternal) {
            const event = new TableEventAddModel();
            event.source = this.source;

            this.create.emit(event);
        } else {
            this.table.grid.createFormShown = true;
        }
    }

    deleteConfirm(event: TableEventDeleteModel<any>) {
        delete event.data['customActions'];

        this.delete.emit(event);
    }

    editConfirm(event: TableEventEditModel<any>) {
        delete event.data['customActions'];
        delete event.newData['customActions'];

        this.edit.emit(event);
    }

    createConfirm(event: TableEventAddModel<any>) {
        delete event.newData['customActions'];

        this.create.emit(event);
    }

    setActionAdd(isAdd: boolean) {
        this.actionAdd = isAdd;
    }
}

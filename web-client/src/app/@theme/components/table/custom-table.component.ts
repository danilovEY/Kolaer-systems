import {Component, Input, OnInit} from '@angular/core';
import {DataSource} from 'ng2-smart-table/lib/data-source/data-source';
import {CustomActionEditComponent} from './custom-action-edit.component';
import {CustomActionViewComponent} from './custom-action-view.component';
import {Column} from 'ng2-smart-table/lib/data-set/column';

@Component({
    selector: 'custom-table',
    template: `
        <ng2-smart-table #table [settings]="settings" [source]="source">
        </ng2-smart-table>
    `
})
export class CustomTableComponent implements OnInit {
    @Input() source: DataSource;
    @Input() actionAdd: boolean = true;
    @Input() actionEdit: boolean = true;
    @Input() actionDelete: boolean = true;

    @Input() columns: Map<string, Column> = new Map<string, Column>();



    settings = {
        actions: {
            columnTitle: 'Действия',
            add: true,
            edit: true,
            delete: true,
            position: 'right',
        },
        pager: {
            display: true,
            perPage: 15,
        },
        noDataMessage: 'Парольница пустая',
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
        columns:  {
            id: {
                title: 'ID',
                type: 'number',
                editable: false,
                addable: false,
                width: '15px',
            },
            name: {
                title: 'Имя',
                type: 'string',
            },
            customActions: {
                title: 'Прочие действия',
                type: 'custom',
                editable: false,
                addable: false,
                filter: false,
                sort: false,
                width: '30px',
                editor: {
                    type: 'custom',
                    component: CustomActionEditComponent,
                },
                renderComponent: CustomActionViewComponent,
                onComponentInitFunction(instance: CustomActionViewComponent) {
                    instance.actions = [
                        {
                            name: 'open',
                            content: '<i class="fa fa-eye"></i>',
                            description: 'Открыть'
                        }
                    ];
                    instance.custom.asObservable().subscribe(value => {
                        console.log(value);
                    });
                }
            }
        },
    };

    ngOnInit() {
        this.settings.actions.add = this.actionAdd;
        this.settings.actions.edit = this.actionEdit;
        this.settings.actions.delete = this.actionDelete;

    }

}

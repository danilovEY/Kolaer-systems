import {Component, OnInit, ViewChild} from '@angular/core';
import {DataSource} from 'ng2-smart-table/lib/data-source/data-source';
import {KolpassService} from './kolpass.service';
import {CustomTableComponent} from '../../../@theme/components';
import {Column} from 'ng2-smart-table/lib/data-set/column';
import {CustomActionModel} from '../../../@theme/components/table/custom-action.model';
import {RepositoryPasswordDataSource} from './repository-password.data-source';

@Component({
    selector: 'app-kolpass',
    styleUrls: ['./kolpass.component.scss'],
    templateUrl: './kolpass.component.html'
})
export class KolpassComponent implements OnInit {
    
    @ViewChild('customTable')
    customTable: CustomTableComponent;
    
    loading: boolean = true;
    columns: Column[] = [];
    actions: CustomActionModel[] = [];
    source: DataSource;

    constructor(
                private kolpassService: KolpassService) {
        const source: RepositoryPasswordDataSource = new RepositoryPasswordDataSource(kolpassService);
        source.onLoading().subscribe(load => this.loading = load);

        this.source = source;
    }

    ngOnInit(): void {
        const idColumn: Column = new Column('id', {
            title: 'ID',
            type: 'number',
            editable: false,
            addable: false,
            width: '15px',
        }, undefined);

        const nameColumn: Column = new Column('name', {
            title: 'Имя',
            type: 'string',
        }, undefined);

        this.columns.push(idColumn, nameColumn);

        const openAction: CustomActionModel = new CustomActionModel();
        openAction.name = 'open';
        openAction.content = '<i class="fa fa-eye"></i>';
        openAction.description = 'Открыть';

        const copyPassAction: CustomActionModel = new CustomActionModel();
        copyPassAction.name = 'copy-login';
        copyPassAction.content = '<i class="fa fa-key"></i>';
        copyPassAction.description = 'Копировать последний пароль';

        const copyLoginAction: CustomActionModel = new CustomActionModel();
        copyLoginAction.name = 'copy-login';
        copyLoginAction.content = '<i class="fa fa-user-secret"></i>';
        copyLoginAction.description = 'Копировать последний логин';

        this.actions.push(openAction, copyLoginAction, copyPassAction);

        // this.customTable.actionBeforeValueView = this.actionBeforeValueView;
    }

    action(event: any) {
        const selBox = document.createElement('textarea');
        selBox.style.position = 'fixed';
        selBox.style.left = '0';
        selBox.style.top = '0';
        selBox.style.opacity = '0';
        selBox.value = event.name;
        document.body.appendChild(selBox);
        selBox.focus();
        selBox.select();
        document.execCommand('copy');
        document.body.removeChild(selBox);
        
        console.log(event);
    }
    //
    // actionBeforeValueView(action: CustomActionModel, data: any): boolean {
    //     return action.name !== 'open';
    // }
}

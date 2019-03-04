import {Component, OnDestroy, OnInit, Renderer2, ViewChild} from '@angular/core';
import {KolpassService} from '../kolpass.service';
import {CustomTableComponent} from '../../../../@theme/components';
import {Column} from 'ng2-smart-table/lib/data-set/column';
import {CustomActionModel} from '../../../../@theme/components/table/custom-action.model';
import {RepositoryPasswordDataSource} from './repository-password.data-source';
import {CustomActionEventModel} from '../../../../@theme/components/table/custom-action-event.model';
import {RepositoryPasswordModel} from '../repository-password.model';
import {PasswordHistoryModel} from '../password-history.model';
import {ClipboardService} from 'ngx-clipboard';
import {Toast, ToasterConfig, ToasterService} from 'angular2-toaster';
import {ServerExceptionModel} from '../../../../@core/models/server-exception.model';
import {Router} from '@angular/router';
import {TableEventEditModel} from '../../../../@theme/components/table/table-event-edit.model';
import {TableEventAddModel} from '../../../../@theme/components/table/table-event-add.model';
import {TableEventDeleteModel} from '../../../../@theme/components/table/table-event-delete.model';
import {RepositoryPasswordShareDataSource} from './repository-password-share.data-source';
import {Cell} from 'ng2-smart-table';
import {finalize} from 'rxjs/internal/operators';
import {Title} from "@angular/platform-browser";

@Component({
    selector: 'repositories',
    styleUrls: ['./repositories.component.scss'],
    templateUrl: './repositories.component.html'
})
export class RepositoriesComponent implements OnInit, OnDestroy {
    private readonly openActionName: string = 'open';
    private readonly copyPassActionName: string = 'copy-pass';
    private readonly copyLoginActionName: string = 'copy-login';

    @ViewChild('myPassTable')
    myPassTable: CustomTableComponent;

    shareLoading: boolean = true;
    shareColumns: Column[] = [];
    shareActions: CustomActionModel[] = [];
    shareSource: RepositoryPasswordShareDataSource;

    myPassLoading: boolean = true;
    myPassColumns: Column[] = [];
    myPassActions: CustomActionModel[] = [];
    myPassSource: RepositoryPasswordDataSource;


    private passwordHistoryForCopy: PasswordHistoryModel;
    private activeAction: CustomActionModel;
    private errorResponse: ServerExceptionModel;

    config: ToasterConfig = new ToasterConfig({
        positionClass: 'toast-top-right',
        timeout: 5000,
        newestOnTop: true,
        tapToDismiss: true,
        preventDuplicates: false,
        animation: 'fade',
        limit: 5,
    });

    constructor(private toasterService: ToasterService,
                private router: Router,
                private renderer: Renderer2,
                private chipboardService: ClipboardService,
                private kolpassService: KolpassService,
                private titleService: Title
    ) {
        this.titleService.setTitle('Список паролей');

        this.myPassSource = new RepositoryPasswordDataSource(kolpassService);
        this.myPassSource.onLoading().subscribe(load => this.myPassLoading = load);

        this.shareSource = new RepositoryPasswordShareDataSource(kolpassService);
        this.shareSource.onLoading().subscribe(load => this.shareLoading = load);
    }

    ngOnInit(): void {
        const nameColumn: Column = new Column('name', {
            title: 'Имя',
            type: 'string',
        }, undefined);

        const initialsColumn: Column = new Column('initials', {
            title: 'Пользователь',
            type: 'string',
            valuePrepareFunction(a: any, value: RepositoryPasswordModel, cell: Cell) {
                if (value.account) {
                    return value.account.employee ? value.account.employee.initials : value.account.username;
                } else {
                    return '';
                }
            }
        }, undefined);

        this.myPassColumns.push(nameColumn);

        this.shareColumns.push(nameColumn, initialsColumn);

        const openAction: CustomActionModel = new CustomActionModel();
        openAction.name = this.openActionName;
        openAction.content = '<i class="fa fa-eye"></i>';
        openAction.description = 'Открыть';

        const copyPassAction: CustomActionModel = new CustomActionModel();
        copyPassAction.name = this.copyPassActionName;
        copyPassAction.content = '<i class="fa fa-key"></i>';
        copyPassAction.description = 'Копировать последний пароль';

        const copyLoginAction: CustomActionModel = new CustomActionModel();
        copyLoginAction.name = this.copyLoginActionName;
        copyLoginAction.content = '<i class="fa fa-user-secret"></i>';
        copyLoginAction.description = 'Копировать последний логин';

        this.shareActions.push(openAction, copyLoginAction, copyPassAction);
        this.myPassActions.push(openAction, copyLoginAction, copyPassAction);

        // this.myPassTable.actionBeforeValueView = this.actionBeforeValueView;
    }

    myPassAction(event: CustomActionEventModel<RepositoryPasswordModel>) {
        this.passwordHistoryForCopy = undefined;
        this.activeAction = undefined;
        this.errorResponse = undefined;


        if (event.action.name === this.copyLoginActionName || event.action.name === this.copyPassActionName) {
            this.myPassLoading = true;
            this.kolpassService.getLastHistoryByRepository(event.data.id)
                .pipe(finalize(() => this.myPassLoading = false))
                .subscribe(
                    (value: PasswordHistoryModel) => {
                        this.passwordHistoryForCopy = value;
                        this.activeAction = event.action;
                    },
                    responseError => {
                        this.errorResponse = responseError.error;
                        const toast: Toast = {
                            type: 'error',
                            title: 'Ошибка в операции',
                            body: this.errorResponse.message,
                        };

                        this.toasterService.popAsync(toast);
                    });

            this.copyPasswordHistoryToChipboard();
        } else if (event.action.name === this.openActionName) {
            this.router.navigate([`/pages/app/kolpass/repository/${event.data.id}`]);
        }
    }

    /**
     * BUG copy to chipboard after observer
     * @param {number} index
     */
    copyPasswordHistoryToChipboard(index: number = 5): void {
        if (index > 0 && !this.errorResponse) {
            setTimeout(() => {
                if (this.passwordHistoryForCopy) {
                    const copyText = this.activeAction.name === this.copyLoginActionName
                        ? this.passwordHistoryForCopy.login
                        : this.passwordHistoryForCopy.password;

                    const copyTextForNotifyBody = this.activeAction.name === this.copyLoginActionName
                        ? 'Логин скопирован'
                        : 'Пароль скопирован';

                    let toast: Toast;

                    if (this.chipboardService.copyFromContent(copyText)) {
                        toast = {
                            type: 'info',
                            title: 'Успешная операция',
                            body: copyTextForNotifyBody,
                        };
                    } else {
                        toast = {
                            type: 'error',
                            title: 'Ошибка в операции',
                            body: 'Не удалось скопировать запись',
                        };
                    }

                    this.toasterService.popAsync(toast);
                } else {
                    this.copyPasswordHistoryToChipboard(index - 1);
                }
            }, 500);
        }
    }

    ngOnDestroy(): void {
        const elements = document.getElementsByTagName('nb-popover'); // BUG POPUP

        for (let index = elements.length - 1; index >= 0; index--) {
            this.renderer.setStyle(elements[index], 'display', 'none');
        }
    }

    myPassEdit(event: TableEventEditModel<RepositoryPasswordModel>) {
        if (event.newData.name.length > 0) {
            this.kolpassService.editRepository(event.newData)
                .subscribe((value: RepositoryPasswordModel) => event.confirm.resolve(value));
        } else {
            event.confirm.reject();
        }
    }

    myPassCreate(event: TableEventAddModel<RepositoryPasswordModel>) {
        if (event.newData.name.length > 0) {
            this.kolpassService.addRepository(event.newData)
                .subscribe((value: RepositoryPasswordModel) => event.confirm.resolve(value));
        } else {
            event.confirm.reject();
        }
    }

    myPassDelete(event: TableEventDeleteModel<RepositoryPasswordModel>) {
            this.kolpassService.deleteRepository(event.data.id)
                .subscribe(value => event.confirm.resolve());
    }

    //
    // actionBeforeValueView(action: CustomActionModel, data: any): boolean {
    //     return action.name !== 'open';
    // }
}

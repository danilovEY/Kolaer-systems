import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {KolpassService} from '../kolpass.service';
import {ToasterConfig} from 'angular2-toaster';
import {Column} from 'ng2-smart-table/lib/data-set/column';
import {CustomTableComponent} from '../../../../@theme/components';
import {PasswordHistoryDataSource} from './password-history.data-source';
import {DatePipe} from '@angular/common';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {PasswordHistoryModel} from '../password-history.model';
import {TableEventDeleteModel} from '../../../../@theme/components/table/table-event-delete.model';
import {HttpErrorResponse} from '@angular/common/http';
import {NgbModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {AccountModel} from '../../../../@core/models/account.model';
import {Cell} from 'ng2-smart-table';
import {AccountEditComponent} from '../../../../@theme/components/table/account-edit.component';
import {TableEventAddModel} from '../../../../@theme/components/table/table-event-add.model';
import {AccountService} from '../../../../@core/services/account.service';
import {SimpleAccountModel} from '../../../../@core/models/simple-account.model';
import {PasswordHistoryShareDataSource} from './password-history-share.data-source';
import {RepositoryPasswordModel} from '../repository-password.model';
import {switchMap, tap} from "rxjs/operators";

@Component({
    selector: 'repository-detailed',
    styleUrls: ['./repository-detailed.component.scss'],
    templateUrl: './repository-detailed.component.html'
})
export class RepositoryDetailedComponent implements OnInit, OnDestroy {
    private sub: any;
    private confirmToDeleteModal: NgbModalRef;
    private repositoryId: number = 0;


    @ViewChild('historyTable')
    customTable: CustomTableComponent;

    @ViewChild('shareTable')
    shareTable: CustomTableComponent;

    formUpdatePass: FormGroup;

    currentPassword: PasswordHistoryModel;
    currentRepository: RepositoryPasswordModel;
    currentAccount: SimpleAccountModel;

    historyColumns: Column[] = [];
    historySource: PasswordHistoryDataSource;

    shareColumns: Column[] = [];
    shareSource: PasswordHistoryShareDataSource;

    loadingLastPass: boolean = true;
    loadingHistory: boolean = true;
    loadingSharingAccounts: boolean = true;
    config: ToasterConfig = new ToasterConfig({
        positionClass: 'toast-top-right',
        timeout: 5000,
        newestOnTop: true,
        tapToDismiss: true,
        preventDuplicates: false,
        animation: 'fade',
        limit: 5,
    });

    constructor(private activatedRoute: ActivatedRoute,
                private modalService: NgbModal,
                private accountService: AccountService,
                private kolpassService: KolpassService) {
        this.sub = this.activatedRoute.params.subscribe(params => {
            this.repositoryId = params['id'];

            this.historySource = new PasswordHistoryDataSource(this.repositoryId, this.kolpassService);
            this.historySource.onLoading().subscribe(load => this.loadingHistory = load);
        });
    }

    ngOnInit() {
        this.formUpdatePass = new FormGroup({
            login: new FormControl('', [Validators.required]),
            password: new FormControl(''),
        }, (g: FormGroup) => this.currentPassword &&
            g.get('login').value === this.currentPassword.login &&
            g.get('password').value === this.currentPassword.password
            ? {'mismatch': true} : null
        );

        this.accountService.getCurrentAccount()
            .pipe(
                tap((account: SimpleAccountModel) => this.currentAccount = account),
                switchMap((account: SimpleAccountModel) =>
                    this.kolpassService.getRepository(this.repositoryId)
                        .pipe(
                            tap((response: RepositoryPasswordModel) => {
                                this.currentRepository = response;

                                if (!response.account) {
                                    this.shareSource = new PasswordHistoryShareDataSource(this.repositoryId, this.kolpassService);
                                    this.shareSource.onLoading().subscribe(load => this.loadingSharingAccounts = load);
                                } else {
                                    this.formUpdatePass.get('login').disable();
                                    this.formUpdatePass.get('password').disable();

                                    this.customTable.settings.actions.delete = false;
                                    this.customTable.table.initGrid();
                                }
                            })
                        ))
            ).subscribe((response: RepositoryPasswordModel) => {
            });

        const dateColumn: Column = new Column('passwordWriteDate', {
            title: 'Время создания',
            type: 'date',
            valuePrepareFunction(value: number) {
                const datePipe = new DatePipe('en-US');
                return datePipe.transform(new Date(value), 'dd.MM.yyyy hh:mm:ss');
            }
        }, undefined);

        const loginColumn: Column = new Column('login', {
            title: 'Логин',
            type: 'string',
        }, undefined);

        const passColumn: Column = new Column('password', {
            title: 'Пароль',
            type: 'string',
        }, undefined);

        this.historyColumns.push(loginColumn, passColumn, dateColumn);

        const accountColumn: Column = new Column('initials', {
            title: 'Пользователь',
            type: 'string',
            sort: false,
            filter: false,
            editor: {
                type: 'custom',
                component: AccountEditComponent,
            },
            valuePrepareFunction(a: any, value: AccountModel, cell: Cell) {
                return value.employee ? value.employee.initials : value.username;
            }
        }, undefined);

        const postColumn: Column = new Column('employeePost', {
            title: 'Должность',
            type: 'string',
            editable: false,
            addable: false,
            sort: false,
            filter: false,
            valuePrepareFunction(a: any, value: AccountModel, cell: Cell) {
                return value.employee ? value.employee.post.abbreviatedName : '';
            }
        }, undefined);

        const departmentColumn: Column = new Column('employeeDepartment', {
            title: 'Подразделние',
            type: 'string',
            editable: false,
            addable: false,
            sort: false,
            filter: false,
            valuePrepareFunction(a: any, value: AccountModel, cell: Cell) {
                return value.employee ? value.employee.department.abbreviatedName : '';
            }
        }, undefined);

        this.shareColumns.push(accountColumn, postColumn, departmentColumn);

        if (this.repositoryId > 0) {
           this.loadLastPass();
        }
    }

    private loadLastPass(): void {
        this.currentPassword = undefined;
        this.loadingLastPass = true;

        this.kolpassService.getLastHistoryByRepository(this.repositoryId)
            .finally(() => this.loadingLastPass = false)
            .subscribe(
                (value: PasswordHistoryModel) => {
                    this.currentPassword = value;

                    this.formUpdatePass.controls['login'].setValue(value.login);
                    this.formUpdatePass.controls['password'].setValue(value.password);
                },
                (responseError: HttpErrorResponse) => {
                    if (responseError.status === 404) {
                        this.formUpdatePass.controls['login'].setValue('');
                        this.formUpdatePass.controls['password'].setValue('');
                    }
                });
    }

    ngOnDestroy() {
        this.sub.unsubscribe();
    }

    shareCreate(event: TableEventAddModel<any>) {
        const account: AccountModel = event.newData.initials;
        if (account.id === this.currentAccount.id) {
            event.confirm.reject();
        } else {
            this.kolpassService.shareRepositoryToAccount(this.repositoryId, account)
                .subscribe(
                    response => event.confirm.resolve(),
                    error => event.confirm.reject()
                );
        }
    }

    shareDelete(event: TableEventDeleteModel<AccountModel>) {
        if (confirm(`Вы действительно хотите удалить ${event.data.employee ? event.data.employee.initials : event.data.username}?`)) {
            this.kolpassService.deleteShareRepositoryToAccount(this.repositoryId, event.data)
                .subscribe(
                    response => event.confirm.resolve(),
                    error => event.confirm.reject()
                );
        } else {
            event.confirm.reject();
        }
    }

    historyDelete(event: TableEventDeleteModel<PasswordHistoryModel>) {
        this.loadingHistory = true;
        if (this.currentPassword && this.currentPassword.id === event.data.id) {
            this.loadingLastPass = true;
        }

        this.kolpassService.removeHistoryFromRepository(this.repositoryId, event.data.id)
            .finally(() => {
                this.loadingHistory = false;
                this.loadingLastPass = false;
            })
            .subscribe(value => {
                event.confirm.resolve();

                if (this.currentPassword && this.currentPassword.id === event.data.id) {
                    this.loadLastPass();
                }
            });
    }

    confirmToDelete(content) {
        this.confirmToDeleteModal = this.modalService.open(content, {size: 'lg', container: 'nb-layout'});
    }

    submitUpdatePassForm() {
        this.loadingLastPass = true;
        this.loadingHistory = true;

        const newPassword: PasswordHistoryModel = new PasswordHistoryModel();
        newPassword.login = this.formUpdatePass.controls['login'].value;
        newPassword.password = this.formUpdatePass.controls['password'].value;

        this.kolpassService.addPasswordToRepository(this.repositoryId, newPassword)
            .finally(() => {
                this.loadingLastPass = false;
                this.loadingHistory = false;
            })
            .subscribe((value: PasswordHistoryModel) => {
                this.currentPassword = value;

                this.formUpdatePass.controls['login'].setValue(value.login);
                this.formUpdatePass.controls['password'].setValue(value.password);

                this.historySource.prepend(value);
            });
    }

    clearHistory() {
        if (this.confirmToDeleteModal) {
            this.confirmToDeleteModal.close();
        }

        this.loadingLastPass = true;
        this.loadingHistory = true;

        this.kolpassService.clearHistoryFromRepository(this.repositoryId)
            .finally(() => {
                this.loadingLastPass = false;
                this.loadingHistory = false;
            })
            .subscribe(value => {
                this.formUpdatePass.controls['login'].setValue('');
                this.formUpdatePass.controls['password'].setValue('');
                this.currentPassword = undefined;

                this.historySource.load([]);
            });
    }
}

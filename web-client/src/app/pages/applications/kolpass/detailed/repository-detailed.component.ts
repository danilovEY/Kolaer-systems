import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {KolpassService} from '../kolpass.service';
import {ToasterConfig} from 'angular2-toaster';
import {Column} from 'ng2-smart-table/lib/data-set/column';
import {CustomTableComponent} from '../../../../@theme/components';
import {PasswordHistoryDataSource} from '../password-history.data-source';
import {DatePipe} from '@angular/common';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {PasswordHistoryModel} from '../password-history.model';
import {TableEventDeleteModel} from '../../../../@theme/components/table/table-event-delete.model';
import {HttpErrorResponse} from '@angular/common/http';
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";

@Component({
    selector: 'repository-detailed',
    styleUrls: ['./repository-detailed.component.scss'],
    templateUrl: './repository-detailed.component.html'
})
export class RepositoryDetailedComponent implements OnInit, OnDestroy {
    private sub: any;
    private confirmToDeleteModal: NgbModalRef;
    private repositoryId: number = 0;

    @ViewChild('customTable')
    customTable: CustomTableComponent;

    formUpdatePass: FormGroup;

    currentPassword: PasswordHistoryModel;
    columns: Column[] = [];
    source: PasswordHistoryDataSource;
    loadingLastPass: boolean = true;
    loadingHistory: boolean = true;
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
                private kolpassService: KolpassService) {
        this.sub = this.activatedRoute.params.subscribe(params => {
            this.repositoryId = params['id'];
            this.source = new PasswordHistoryDataSource(this.repositoryId, this.kolpassService);
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

        // const idColumn: Column = new Column('id', {
        //     title: 'ID',
        //     type: 'number',
        //     editable: false,
        //     addable: false,
        // }, undefined);

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

        this.columns.push(loginColumn, passColumn, dateColumn);

        if (this.repositoryId > 0) {
            this.source.onLoading().subscribe(load => this.loadingHistory = load);

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

    delete(event: TableEventDeleteModel<PasswordHistoryModel>) {
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

                this.source.prepend(value);
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

                this.source.load([]);
            });
    }
}

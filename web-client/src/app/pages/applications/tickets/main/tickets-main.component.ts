import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {CustomTableComponent} from '../../../../@theme/components';
import {DatePipe} from '@angular/common';
import {Column} from 'ng2-smart-table/lib/data-set/column';
import {Toast, ToasterConfig, ToasterService} from 'angular2-toaster';
import {TicketsRegisterDataSource} from '../tickents-register.data-source';
import {TableEventDeleteModel} from '../../../../@theme/components/table/table-event-delete.model';
import {TicketRegisterModel} from '../ticket-register.model';
import {TicketsService} from '../tickets.service';
import {CustomActionModel} from '../../../../@theme/components/table/custom-action.model';
import {CustomActionEventModel} from '../../../../@theme/components/table/custom-action-event.model';
import {TableEventAddModel} from '../../../../@theme/components/table/table-event-add.model';
import {environment} from '../../../../../environments/environment';
import {UploadFileModel} from '../../../../@core/models/upload-file.model';
import {AccountService} from '../../../../@core/services/account.service';
import {SimpleAccountModel} from '../../../../@core/models/simple-account.model';
import {NgbModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {ReportTicketsConfigModel} from '../report-tickets-config.model';
import {FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
    selector: 'main-tickets',
    styleUrls: ['./tickents-main.component.scss'],
    templateUrl: './tickents-main.component.html'
})
export class TicketsMainComponent implements OnInit {
    private static readonly openRegisterActionName = 'open';
    private static readonly generateAndDownloadActionName = 'download';
    private static readonly generateAndSendActionName = 'send';

    private confirmToSendModal: NgbModalRef;
    private createRegisterModal: NgbModalRef;
    private selectedTicketRegister: TicketRegisterModel;
    private send: boolean = false;

    @ViewChild('customTable')
    customTable: CustomTableComponent;

    @ViewChild('generateReportModalElement')
    generateReportModalElement: ElementRef;

    @ViewChild('createRegisterModal')
    createRegisterModalElement: ElementRef;

    createRegisterForm: FormGroup;

    columns: Column[] = [];
    actions: CustomActionModel[] = [];
    source: TicketsRegisterDataSource;
    loadingRegisters: boolean = true;
    config: ToasterConfig = new ToasterConfig({
        positionClass: 'toast-top-right',
        timeout: 5000,
        newestOnTop: true,
        tapToDismiss: true,
        preventDuplicates: false,
        animation: 'fade',
        limit: 5,
    });
    isInTime: boolean = false;
    inTime: Date = new Date();

    constructor(private ticketsService: TicketsService,
                private toasterService: ToasterService,
                private modalService: NgbModal,
                private accountService: AccountService) {
        this.source = new TicketsRegisterDataSource(this.ticketsService);
        this.source.onLoading().subscribe(value => this.loadingRegisters = value);
    }

    ngOnInit() {
        this.createRegisterForm = new FormGroup({
            countTickets: new FormControl(0, [Validators.required, Validators.min(0), Validators.max(99)]),
            type: new FormControl('DR'),
        });

        const idColumn: Column = new Column('id', {
            title: 'ID',
            type: 'number',
            editable: false,
            addable: false,
        }, undefined);

        const dateCreateColumn: Column = new Column('createRegister', {
            title: 'Время создания',
            type: 'date',
            editable: false,
            addable: false,
            valuePrepareFunction(value: string) {
                const datePipe = new DatePipe('en-US');
                return datePipe.transform(new Date(value), 'dd.MM.yyyy HH:mm:ss');
            }
        }, undefined);

        const dateSendColumn: Column = new Column('sendRegisterTime', {
            title: 'Время отправления',
            type: 'date',
            editable: false,
            addable: false,
            valuePrepareFunction(value: string) {
                if (value) {
                    const datePipe = new DatePipe('en-US');
                    return datePipe.transform(new Date(value), 'dd.MM.yyyy HH:mm:ss');
                } else {
                    return undefined;
                }


            }
        }, undefined);

        this.columns.push(idColumn, dateCreateColumn, dateSendColumn);

        const openAction: CustomActionModel = new CustomActionModel();
        openAction.name = TicketsMainComponent.openRegisterActionName;
        openAction.content = '<i class="fa fa-eye"></i>';
        openAction.description = 'Открыть';

        this.actions.push(openAction);

        this.accountService.getCurrentAccount()
            .subscribe((account: SimpleAccountModel) => {
               if (account && account.accessOit) {
                   const generateAndDownloadAction: CustomActionModel = new CustomActionModel();
                   generateAndDownloadAction.name = TicketsMainComponent.generateAndDownloadActionName;
                   generateAndDownloadAction.content = '<i class="fa fa-download"></i>';
                   generateAndDownloadAction.description = 'Сформировать отчет и скачать';
                   this.actions.push(generateAndDownloadAction);
               }
            });

        const generateAndSendAction: CustomActionModel = new CustomActionModel();
        generateAndSendAction.name = TicketsMainComponent.generateAndSendActionName;
        generateAndSendAction.content = '<i class="fa fa-envelope"></i>';
        generateAndSendAction.description = 'Сформировать отчет и отправить';

        this.actions.push(generateAndSendAction);


        this.customTable.actionBeforeValueView = this.actionBeforeValueView;
    }

    delete(event: TableEventDeleteModel<TicketRegisterModel>) {
        console.log(event);
    }

    create(event: TableEventAddModel<TicketRegisterModel>) {
        console.log(event);
    }

    action(event: CustomActionEventModel<TicketRegisterModel>) {
        if (event.action.name === TicketsMainComponent.generateAndDownloadActionName) {
            const attachment: UploadFileModel = event.data.attachment;
            if (attachment !== undefined && attachment !== null) {
                window.open(`${environment.publicServerUrl}/upload/file/${attachment.id}/${attachment.fileName}`);
            } else {
                this.send = false;
                this.selectedTicketRegister = event.data;
                this.confirmToSendModal = this.modalService.open(this.generateReportModalElement, {size: 'lg', container: 'nb-layout'});
            }
        } else if (event.action.name === TicketsMainComponent.generateAndSendActionName) {
            this.send = true;
            this.selectedTicketRegister = event.data;
            this.confirmToSendModal = this.modalService.open(this.generateReportModalElement, {size: 'lg', container: 'nb-layout'});
        }
    }

    actionBeforeValueView(event: CustomActionEventModel<TicketRegisterModel>): boolean {
        if (event.action.name === TicketsMainComponent.generateAndSendActionName) {
           return event.data.sendRegisterTime !== undefined;
        }

        if (event.action.name === TicketsMainComponent.generateAndDownloadActionName && event.data.attachment) {
            event.action.description = 'Скачать отчет';
        }

        return true;
    }

    generateReportAndSend() {
        if (this.selectedTicketRegister) {
            const datePipe = new DatePipe('en-US');
            const config: ReportTicketsConfigModel =
                new ReportTicketsConfigModel(!this.isInTime, datePipe.transform(this.inTime, 'yyyy-MM-dd\'T\'HH:mm:ss'));
            if (this.send) {
                this.ticketsService.generateAndSendReport(this.selectedTicketRegister.id, config)
                    .subscribe((value: TicketRegisterModel) => {
                        this.source.update(this.selectedTicketRegister, value);

                        const toast: Toast = {
                            type: 'success',
                            title: 'Успешная операция',
                            body: 'Отчет был отправлен на email',
                        };

                        this.toasterService.popAsync(toast);
                    });
            } else {
                this.ticketsService.generateReportAndDownloadUrl(this.selectedTicketRegister.id, config)
                    .subscribe((value: TicketRegisterModel) => {
                        this.source.update(this.selectedTicketRegister, value);

                        const toast: Toast = {
                            type: 'success',
                            title: 'Успешная операция',
                            body: 'Отчет сформирован',
                        };

                        this.toasterService.popAsync(toast);

                        const attachment: UploadFileModel = value.attachment;
                        if (attachment !== undefined && attachment !== null) {
                            window.open(`${environment.publicServerUrl}/upload/file/${attachment.id}/${attachment.fileName}`);
                        }
                    });
            }

            if (this.confirmToSendModal) {
                this.confirmToSendModal.close({});
            }
        }
    }

    onChangeHeader() {
        this.isInTime = !this.isInTime;
    }

    openCreateRegisterModal() {
        this.createRegisterModal = this.modalService.open(this.createRegisterModalElement, {size: 'lg', container: 'nb-layout'});
    }

    createRegisterSubmit() {
        console.log(this.createRegisterForm);
    }
}

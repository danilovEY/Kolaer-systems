import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {EmployeeService} from '../../../../@core/services/employee.service';
import {finalize} from 'rxjs/internal/operators';
import {HistoryChangeModel} from '../../../../@core/models/history-change.model';
import {HistoryChangeEventEnum} from '../../../../@core/models/history-change-event.enum';
import {ServerExceptionModel} from '../../../../@core/models/server-exception.model';
import {SimpleAccountModel} from '../../../../@core/models/simple-account.model';
import {AccountService} from '../../../../@core/services/account.service';
import {RoleConstant} from "../../../../@core/constants/role.constant";

@Component({
    selector: 'org-structures-sync',
    styleUrls: ['./org-structures-sync.component.scss'],
    templateUrl: './org-structures-sync.component.html'
})
export class OrgStructuresSyncComponent implements OnInit {
    private readonly UPDATE_ENTITY = 'warning';
    private readonly ADD_ENTITY = 'success';
    private readonly DELETE_ENTITY = 'danger';
    private readonly OTHER_ENTITY = 'info';

    @ViewChild('uploadFileInput')
    uploadFileInput: ElementRef;

    loadingUploadFile: boolean = false;
    selectedFiles: FileList;
    currentFileUpload: File;
    histories: HistoryChangeModel[] = [];

    currentAccount: SimpleAccountModel;

    serverError: ServerExceptionModel;

    constructor(private employeeService: EmployeeService,
                private accountService: AccountService) {

    }

    ngOnInit(): void {
        this.accountService.getCurrentAccount()
            .subscribe(account => this.currentAccount = account);
    }

    convertToObject(value: string): any {
        if (value && value.length > 2) {
            return JSON.parse(value);
        }

        return '{}';
    }

    uploadFile() {
        this.serverError = null;
        this.currentFileUpload = this.selectedFiles.item(0);
        this.loadingUploadFile = true;
        this.employeeService.uploadFileForSync(this.currentFileUpload)
            .pipe(finalize(() => {
                this.loadingUploadFile = false;
                this.uploadFileInput.nativeElement.value = '';
            }))
            .subscribe((histories: HistoryChangeModel[]) => this.histories = histories,
                error2 => this.serverError = error2.error);

        this.selectedFiles = undefined
    }

    selectFile(event) {
        this.selectedFiles = event.target.files;
    }

    getType(historyChange: HistoryChangeModel): string {
        if (!historyChange || !historyChange.event) {
            return this.OTHER_ENTITY;
        }

        if (historyChange.event === HistoryChangeEventEnum.UPDATE_DEPARTMENT ||
            historyChange.event === HistoryChangeEventEnum.UPDATE_POST ||
            historyChange.event === HistoryChangeEventEnum.UPDATE_EMPLOYEE) {
            return this.UPDATE_ENTITY;
        }

        if (historyChange.event === HistoryChangeEventEnum.ADD_DEPARTMENT ||
            historyChange.event === HistoryChangeEventEnum.ADD_POST ||
            historyChange.event === HistoryChangeEventEnum.ADD_EMPLOYEE) {
            return this.ADD_ENTITY;
        }

        if (historyChange.event === HistoryChangeEventEnum.HIDE_DEPARTMENT ||
            historyChange.event === HistoryChangeEventEnum.HIDE_POST ||
            historyChange.event === HistoryChangeEventEnum.HIDE_EMPLOYEE) {
            return this.DELETE_ENTITY;
        }

        return this.OTHER_ENTITY;
    }

    sendReportForOldDb(): void {
        this.employeeService.sendReportForOldDb()
            .subscribe(event => {})
    }

    canSendReportForOld(): boolean {
        return this.currentAccount.access.includes(RoleConstant.EMPLOYEES_REPORT_OLD);
    }
}

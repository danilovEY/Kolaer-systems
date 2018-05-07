import {Component, OnInit, ViewChild} from '@angular/core';
import {AccountService} from '../../services/account.service';
import {AccountModel} from '../../models/account.model';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {environment} from '../../../../environments/environment';
import {ServerExceptionModel} from '../../models/server-exception.model';
import {Observable} from 'rxjs/Observable';
import {AuthenticationRestService} from '../../modules/auth/authentication-rest.service';
import {ModalDirective} from 'angular-bootstrap-md/modals/modal.directive';
import {ChangePasswordModel} from "../../models/change-password.model";

@Component({
    selector: 'app-setting',
    templateUrl: './setting.component.html',
    styleUrls: ['./setting.component.scss']
})
export class SettingComponent implements OnInit {
    private readonly updateAccountUrl: string = `${environment.publicServerUrl}/user/update`;
    private readonly updatePasswordUrl: string = `${environment.publicServerUrl}/user/update/password`;

    loadCurrentAccount: boolean = true;
    currentAccount: AccountModel;

    serverError: ServerExceptionModel;

    accountForm: FormGroup;
    changePassForm: FormGroup;

    @ViewChild('changePasswordModal')
    changePasswordModal: ModalDirective;

    @ViewChild('successModal')
    successModal: ModalDirective;

    constructor(private _authService: AuthenticationRestService,
                private _accountService: AccountService,
                private _httpClient: HttpClient) {
    }

    ngOnInit() {
        this.accountForm = new FormGroup({
            login: new FormControl('', [Validators.minLength(3)]),
            email: new FormControl('', [Validators.email]),
            chatName: new FormControl('', [Validators.minLength(3)]),
        });

        this.changePassForm = new FormGroup({
            oldPassword: new FormControl('', []),
            newPassword: new FormControl('', []),
        });

        this._accountService.getCurrentAccount()
            .finally(() => this.loadCurrentAccount = false)
            .subscribe(account => {
                this.currentAccount = account;

                this.accountForm.controls['chatName'].setValue(account.chatName);
                this.accountForm.controls['login'].setValue(account.username);
                this.accountForm.controls['email'].setValue(account.email);
            });
    }

    submitAccountForm() {
        const currentAccountToSend: AccountModel = new AccountModel();
        currentAccountToSend.chatName = this.accountForm.value.chatName;
        currentAccountToSend.username = this.accountForm.value.login;
        currentAccountToSend.email = this.accountForm.value.email;
        currentAccountToSend.accessOit = this.currentAccount.accessOit;
        currentAccountToSend.accessUser = this.currentAccount.accessUser;
        currentAccountToSend.employee = this.currentAccount.employee;
        currentAccountToSend.id = this.currentAccount.id;

        this._httpClient.post(this.updateAccountUrl, currentAccountToSend)
            .subscribe(
                (result: any) => {
                    if (currentAccountToSend.username !== this.currentAccount.username) {
                        this._authService.logout().subscribe(Observable.empty);
                    }
                },
                (error: HttpErrorResponse) => {
                    this.serverError = error.error;
                });
    }

    sibmitUpdatePassword() {
        const changePassword: ChangePasswordModel = new ChangePasswordModel();
        changePassword.oldPassword = this.changePassForm.value.oldPassword;
        changePassword.newPassword = this.changePassForm.value.newPassword;

        this._httpClient.post(this.updatePasswordUrl, changePassword)
            .finally(() => {
                this.changePasswordModal.hide();
                this.changePassForm.reset();
            })
            .subscribe(
                (result: any) => {
                    this.successModal.show();

                    setTimeout(() => {
                        if (this.successModal.isShown) {
                            this.successModal.hide();
                        }

                        this._authService.logout().subscribe(Observable.empty);
                    }, 2000)
                },
                (error: HttpErrorResponse) => {
                    this.serverError = error.error;
                });
    }

}


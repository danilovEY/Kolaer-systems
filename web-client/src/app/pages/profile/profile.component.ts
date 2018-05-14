import {Component, OnInit} from '@angular/core';
import {AccountService} from '../../@core/services/account.service';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {environment} from '../../../environments/environment';
import {Observable} from 'rxjs/Observable';
import {AuthenticationRestService} from '../../@core/modules/auth/authentication-rest.service';
import {ServerExceptionModel} from '../../@core/models/server-exception.model';
import {ChangePasswordModel} from '../../@core/models/change-password.model';
import {NgbModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {SimpleAccountModel} from '../../@core/models/simple-account.model';
import {EmployeeService} from "../../@core/services/employee.service";
import {EmployeeModel} from "../../@core/models/employee.model";

@Component({
    selector: 'profile',
    templateUrl: './profile.component.html',
    styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
    private readonly updateAccountUrl: string = `${environment.publicServerUrl}/user/update`;
    private readonly updatePasswordUrl: string = `${environment.publicServerUrl}/user/update/password`;

    currentAccount: SimpleAccountModel;
    serverErrorForChangeAccount: ServerExceptionModel;
    serverErrorForChangePassword: ServerExceptionModel;
    successChangePassword: boolean = false;
    successChangeAccount: boolean = false;
    needLogout: boolean = false;

    formAccount: FormGroup;
    changePassForm: FormGroup;
    openedChangePasswordModal: NgbModalRef;

    constructor(private accountService: AccountService,
                private employeeService: EmployeeService,
                private authService: AuthenticationRestService,
                private modalService: NgbModal,
                private httpClient: HttpClient) {
    }

    ngOnInit() {
        this.formAccount = new FormGroup({
            login: new FormControl('', [Validators.minLength(3)]),
            email: new FormControl('', [Validators.email]),
            chatName: new FormControl('', [Validators.minLength(3)]),
        });

        this.changePassForm = new FormGroup({
            oldPassword: new FormControl('', []),
            newPassword: new FormControl('', []),
            confirmPassword: new FormControl('', []),
        }, this.passwordMatchValidator);

       this.updateCurrentAccount();

        this.employeeService.getCurrentEmployee().subscribe(
            (employee: EmployeeModel) => console.log(employee),
                error2 => console.log(error2));
    }

    private updateCurrentAccount(cache: boolean = true) {
        this.accountService
            .getCurrentAccount(cache)
            .subscribe((account: SimpleAccountModel) => {
                this.currentAccount = account;

                this.formAccount.controls['chatName'].setValue(account.chatName);
                this.formAccount.controls['login'].setValue(account.username);
                this.formAccount.controls['email'].setValue(account.email);
            });
    }

    private passwordMatchValidator(g: FormGroup) {
        return g.get('newPassword').value === g.get('confirmPassword').value
            ? null : {'mismatch': true};
    }

    submitAccountForm() {
        this.serverErrorForChangeAccount = undefined;
        this.successChangeAccount = false;
        this.needLogout = false;

        const currentAccountToSend: SimpleAccountModel = new SimpleAccountModel();
        currentAccountToSend.chatName = this.formAccount.value.chatName;
        currentAccountToSend.username = this.formAccount.value.login;
        currentAccountToSend.email = this.formAccount.value.email;
        currentAccountToSend.accessOit = this.currentAccount.accessOit;
        currentAccountToSend.accessUser = this.currentAccount.accessUser;
        currentAccountToSend.employeeId = this.currentAccount.employeeId;
        currentAccountToSend.id = this.currentAccount.id;

        this.httpClient.post(this.updateAccountUrl, currentAccountToSend)
            .subscribe(
                (result: any) => {
                    this.successChangeAccount = true;

                    if (currentAccountToSend.username !== this.currentAccount.username) {
                        this.needLogout = true;
                        setTimeout(() => this.authService.logout(true).subscribe(Observable.empty), 2000);
                    } else {
                        this.updateCurrentAccount(false);
                    }
                },
                (error: HttpErrorResponse) => {
                    this.serverErrorForChangeAccount = error.error;
                });
    }

    openChangePasswordModal(content) {
        this.openedChangePasswordModal = this.modalService.open(content, {size: 'lg', container: 'nb-layout'});
    }

    submitUpdatePassword() {
        this.serverErrorForChangePassword = undefined;
        this.successChangePassword = false;

        const changePassword: ChangePasswordModel = new ChangePasswordModel();
        changePassword.oldPassword = this.changePassForm.value.oldPassword;
        changePassword.newPassword = this.changePassForm.value.newPassword;

        this.httpClient.post(this.updatePasswordUrl, changePassword)
            .finally(() => {
                this.changePassForm.reset();
            })
            .subscribe(
                (result: any) => {
                    this.successChangePassword = true;

                    setTimeout(() => {
                        if (this.openedChangePasswordModal) {
                            this.openedChangePasswordModal.close();
                        }

                        this.authService.logout(true).subscribe(Observable.empty);
                    }, 2000)
                },
                (error: HttpErrorResponse) => {
                    this.serverErrorForChangePassword = error.error;
                });
    }
}


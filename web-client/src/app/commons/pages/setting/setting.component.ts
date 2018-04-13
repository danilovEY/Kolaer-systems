import {Component, OnInit} from '@angular/core';
import {AccountService} from '../../services/account.service';
import {AccountModel} from '../../models/account.model';
import {HttpClient} from '@angular/common/http';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {environment} from '../../../../environments/environment';

@Component({
    selector: 'app-setting',
    templateUrl: './setting.component.html',
    styleUrls: ['./setting.component.scss']
})
export class SettingComponent implements OnInit {
    private readonly updateAccountUrl: string = `${environment.publicServerUrl}/user/update`;

    loadCurrentAccount: boolean = true;
    currentAccount: AccountModel;

    accountForm: FormGroup;

    constructor(private _accountService: AccountService,
                private _httpClient: HttpClient) {
    }

    ngOnInit() {
        this.accountForm = new FormGroup({
            login: new FormControl('', [Validators.minLength(3)]),
            email: new FormControl('', [Validators.email]),
            chatName: new FormControl('', [Validators.minLength(3)]),
        });

        this._accountService.getCurrentAccount()
            .finally(() => this.loadCurrentAccount = false)
            .subscribe(account => {
                this.currentAccount = account;

                this.accountForm.value.chatName = account.chatName;
                this.accountForm.value.login = account.username;
                this.accountForm.value.email = account.email;
            });
    }

    submitAccountForm() {
        this.currentAccount.chatName = this.accountForm.value.chatName;
        this.currentAccount.username = this.accountForm.value.login;
        this.currentAccount.email = this.accountForm.value.email;

        this._httpClient.post(this.updateAccountUrl, this.currentAccount)
            .subscribe(
                (result: AccountModel) => console.log(result),
                    error => console.log(error));
    }
}


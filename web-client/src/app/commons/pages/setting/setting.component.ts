import {Component, OnInit} from '@angular/core';
import {AccountService} from '../../services/account.service';
import {AccountModel} from '../../models/account.model';

@Component({
    selector: 'app-setting',
    templateUrl: './setting.component.html',
    styleUrls: ['./setting.component.scss']
})
export class SettingComponent implements OnInit {
    currentAccount: AccountModel;

    constructor(private _accountService: AccountService) {
    }

    ngOnInit() {
        this._accountService.getCurrentAccount().subscribe(account => this.currentAccount = account );
    }

}

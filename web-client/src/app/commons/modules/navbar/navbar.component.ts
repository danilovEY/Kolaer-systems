import {Component, OnInit} from '@angular/core';
import {AuthenticationServiceRest} from '../auth/authentication.rest.service';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {AuthenticationObserver} from '../../services/authenticationObserver';
import {AccountModel} from '../../models/account.model';

@Component({
	selector: 'app-navbar',
	templateUrl: './navbar.component.html',
	styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit, AuthenticationObserver {
	loginForm: FormGroup;

	constructor(private _authenticationService: AuthenticationServiceRest) {

	}

	ngOnInit(): void {
		this.loginForm = new FormGroup({
			username: new FormControl('', [Validators.required]),
			password: new FormControl(''),
		});

		this._authenticationService.registerObserver(this)
	}

	submitLogin(): void {
		this._authenticationService.login(this.loginForm.value.username, this.loginForm.value.password)
			.subscribe((account: AccountModel) => console.log('Account', account),
				error => console.log('Error', error));
	}

	login(account: AccountModel): void {

	}

	logout(account: AccountModel): void {
	}

}

import {Component, ElementRef, HostListener, Inject, OnInit, Renderer2, ViewChild} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {AuthenticationObserverService} from '../../services/authentication-observer.service';
import {AccountModel} from '../../models/account.model';
import {AuthenticationService} from '../auth/authentication.service';
import {ModalDirective} from 'angular-bootstrap-md/modals/modal.directive';
import {ServerExceptionModel} from '../../models/server-exception.model';
import {AccountService} from "../../services/account.service";

@Component({
	selector: 'app-navbar',
	templateUrl: './navbar.component.html',
	styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit, AuthenticationObserverService {
	loginForm: FormGroup;
	serverError: ServerExceptionModel = undefined;

    accountModel: AccountModel = undefined;

    @ViewChild('loginModal')
	loginModal: ModalDirective;

    @ViewChild('successModal')
	successModal: ModalDirective;

	@ViewChild('nav')
	navbar: ElementRef;


	constructor(@Inject('AuthenticationService') public authenticationService: AuthenticationService,
				public accountService: AccountService,
				private _renderer: Renderer2) {

	}

	ngOnInit(): void {
		this.loginForm = new FormGroup({
			username: new FormControl('', [Validators.required, Validators.minLength(3)]),
			password: new FormControl(''),
		});

		if (this.authenticationService.isAuthentication() && !this.accountModel) {
			this.accountService
				.getCurrentAccount()
				.subscribe(account => this.accountModel = account);
		}

		this.authenticationService.registerObserver(this);
	}

	submitLogin(): void {
		this.serverError = undefined;

		const username: string = this.loginForm.value.username;
		const password: string = this.loginForm.value.password;

		this.authenticationService.login(username, password)
			.subscribe(
				(account: AccountModel) => {
					this.loginModal.hide();
                    this.successModal.show();
                    setTimeout(() => {
                    	if (this.successModal.isShown) {
                            this.successModal.hide();
                        }

                        this.loginForm.reset();
                    }, 2000);
				},
				(error: ServerExceptionModel) => {
					this.serverError = error;
                });
	}

	// @HostListener('window:resize', ['$event'])
	// onResize(event: any) {
		// let breakpoit = 0;

		// if (this.SideClass.includes('navbar-expand-xl')) {
		// 	breakpoit = 1200;
		// } else if (this.SideClass.includes('navbar-expand-lg')) {
		// 	breakpoit = 992;
		// } else if (this.SideClass.includes('navbar-expand-md')) {
		// 	breakpoit = 768;
		// } else if (this.SideClass.includes('navbar-expand-sm')) {
		// 	breakpoit = 576;
		// } else {
		// 	breakpoit = event.target.innerWidth + 1;
		// }

		// if (event.target.innerWidth < breakpoit) {
		// 	if (!this.shown) {
		// 		this.collapse = false;
		// 		this.renderer.setElementStyle(this.el.nativeElement, 'height', '0px');
		// 		this.renderer.setElementStyle(this.el.nativeElement, 'opacity', '0');
		// 		setTimeout(() => {
		// 			this.height = this.el.nativeElement.scrollHeight;
		// 			this.collapse = true;
		// 			this.renderer.setElementStyle(this.el.nativeElement, 'opacity', '');
		// 		}, 4);
		// 	}
		// } else {
		// 	this.collapsing = false;
		// 	this.shown = false;
		// 	this.showClass = false;
		// 	this.collapse = true;
		// 	this.renderer.setElementStyle(this.el.nativeElement, 'height', '');
		// }
	// }

	@HostListener('document:scroll', ['$event'])
	onScroll() {
		if (this.navbar.nativeElement.classList.contains('scrolling-navbar')) {
			if (window.pageYOffset > 120) {
				this._renderer.addClass(this.navbar.nativeElement, 'top-nav-collapse');
			} else {
				this._renderer.removeClass(this.navbar.nativeElement, 'top-nav-collapse');
			}
		}
	}

	login(account: AccountModel): void {
		this.accountModel = account;
	}

	logout(): void {
		this.accountModel = undefined;
	}

	logoutSubmit(): void {
		this.authenticationService.logout()
			.subscribe(
				req => {},
				error => console.log(error)
			);
	}
}

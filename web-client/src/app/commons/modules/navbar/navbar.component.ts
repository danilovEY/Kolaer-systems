import {Component, ElementRef, HostListener, Inject, OnInit, Renderer2, ViewChild} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {AuthenticationObserver} from '../../services/authenticationObserver';
import {AccountModel} from '../../models/account.model';
import {AuthenticationService} from '../../services/authenticationService';

@Component({
	selector: 'app-navbar',
	templateUrl: './navbar.component.html',
	styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit, AuthenticationObserver {
	loginForm: FormGroup;

	@ViewChild('nav') navbar: ElementRef;

	constructor(@Inject('AuthenticationService') private _authenticationService: AuthenticationService,
				private _renderer: Renderer2) {

	}

	ngOnInit(): void {
		this.loginForm = new FormGroup({
			username: new FormControl('', [Validators.required, Validators.minLength(3)]),
			password: new FormControl(''),
		});

		this._authenticationService.registerObserver(this)
	}

	submitLogin(): void {
		this._authenticationService.login(this.loginForm.value.username, this.loginForm.value.password)
			.subscribe(
				(account: AccountModel) => console.log('Account', account),
				error => console.log('Error', error));
	}

	@HostListener('window:resize', ['$event'])
	onResize(event: any) {
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
	}

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

	}

	logout(account: AccountModel): void {
	}

}

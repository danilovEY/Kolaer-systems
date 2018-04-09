import {AfterViewInit, Component, ElementRef, HostListener, Inject, OnInit, Renderer2, ViewChild} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {AuthenticationObserverService} from '../../services/authentication-observer.service';
import {AccountModel} from '../../models/account.model';
import {AuthenticationService} from '../auth/authentication.service';
import {ModalDirective} from 'angular-bootstrap-md/modals/modal.directive';
import {ServerExceptionModel} from '../../models/server-exception.model';
import {AccountService} from '../../services/account.service';
import {Router} from '@angular/router';
import {BsDropdownDirective} from 'angular-bootstrap-md/dropdown/dropdown.directive';

@Component({
    selector: 'app-navbar',
    templateUrl: './navbar.component.html',
    styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit, AfterViewInit, AuthenticationObserverService {

    loginForm: FormGroup;
    serverError: ServerExceptionModel = undefined;

    accountModel: AccountModel = undefined;

    @ViewChild('dropdownMenu')
    dropdownMenu: BsDropdownDirective;

    @ViewChild('loginModal')
    loginModal: ModalDirective;

    @ViewChild('successModal')
    successModal: ModalDirective;

    @ViewChild('nav')
    navbar: ElementRef;


    constructor(@Inject('AuthenticationService') public authenticationService: AuthenticationService,
                public accountService: AccountService,
                private _renderer: Renderer2,
                private _router: Router) {

    }

    ngAfterViewInit(): void {
        if (!this.authenticationService.isAuthentication() && !this.loginModal.isShown &&
            this._router.routerState.snapshot.root.queryParams['login'] !== undefined) {
            this.loginModal.show();
        }
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

    navigateToSetting(): void {
        this._router.navigate(['/home/setting']);
        this.dropdownMenu.hide();
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
                req => {
                },
                error => console.log(error)
            );
    }
}

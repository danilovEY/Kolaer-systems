import {Component, OnInit} from '@angular/core';
import {AccountService} from '../../@core/services/account.service';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {environment} from '../../../environments/environment';
import {AuthenticationRestService} from '../../@core/modules/auth/authentication-rest.service';
import {ServerExceptionModel} from '../../@core/models/server-exception.model';
import {ChangePasswordModel} from '../../@core/models/change-password.model';
import {NgbModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {SimpleAccountModel} from '../../@core/models/simple-account.model';
import {EmployeeService} from '../../@core/services/employee.service';
import {EmployeeModel} from '../../@core/models/employee.model';
import {catchError, finalize, map} from 'rxjs/internal/operators';
import {ContactsService} from '../../@core/services/contacts.service';
import {ContactModel} from '../../@core/models/contact.model';
import {ContactRequestModel} from '../../@core/models/contact-request.model';
import {debounceTime, distinctUntilChanged, switchMap, tap} from 'rxjs/operators';
import {Observable, of, Subject} from 'rxjs/index';
import {SortTypeEnum} from '../../@core/models/sort-type.enum';
import {Page} from '../../@core/models/page.model';
import {PlacementService} from '../../@core/services/placement.service';
import {PlacementFilterModel} from '../../@core/models/placement-filter.model';
import {PlacementModel} from '../../@core/models/placement.model';
import {PlacementSortModel} from '../../@core/models/placement-sort.model';
import {ContactTypeModel} from '../../@core/models/contact-type.model';
import {Toast, ToasterConfig, ToasterService} from 'angular2-toaster';
import {UserService} from '../../@core/services/user.service';

@Component({
    selector: 'profile',
    templateUrl: './profile.component.html',
    styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
    private readonly updateAccountUrl: string = `${environment.publicServerUrl}/user/update`;
    private readonly updatePasswordUrl: string = `${environment.publicServerUrl}/user/update/password`;

    config: ToasterConfig = new ToasterConfig({
        positionClass: 'toast-top-right',
        timeout: 5000,
        newestOnTop: true,
        tapToDismiss: true,
        preventDuplicates: false,
        animation: 'fade',
        limit: 5,
    });

    currentAccount: SimpleAccountModel;
    currentEmployee: EmployeeModel;
    serverErrorForChangeAccount: ServerExceptionModel;
    serverErrorForChangePassword: ServerExceptionModel;
    successChangePassword: boolean = false;
    successChangeAccount: boolean = false;
    needLogout: boolean = false;

    formAccount: FormGroup;
    formContact: FormGroup;
    changePassForm: FormGroup;
    openedChangePasswordModal: NgbModalRef;
    currentContacts: ContactModel;

    people3$: Observable<PlacementModel[]>;
    people3Loading = false;
    people3input$ = new Subject<string>();

    contactTypes: ContactTypeModel[] = [
        ContactTypeModel.MAIN,
        ContactTypeModel.OTHER
    ];

    constructor(private accountService: AccountService,
                private employeeService: EmployeeService,
                private contactsService: ContactsService,
                private userService: UserService,
                private placementService: PlacementService,
                private toasterService: ToasterService,
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

        this.formContact = new FormGroup({
            workPhoneNumber: new FormControl(''),
            mobilePhoneNumber: new FormControl(''),
            pager: new FormControl(''),
            placement: new FormControl(''),
            type: new FormControl(''),
            email: new FormControl('', [Validators.email])
        });

        this.changePassForm = new FormGroup({
            oldPassword: new FormControl('', []),
            newPassword: new FormControl('', []),
            confirmPassword: new FormControl('', []),
        }, this.passwordMatchValidator);

        this.people3$ = this.people3input$.pipe(
            debounceTime(1000),
            distinctUntilChanged(),
            tap(() => this.people3Loading = true),
            switchMap(term => this.placementService
                .getAllPlacements(new PlacementSortModel(null, SortTypeEnum.ASC), new PlacementFilterModel(null, term), 0, 0)
                .pipe(
                    map((request: Page<PlacementModel>) => request.data),
                    catchError(() => of([])),
                    tap(() => this.people3Loading = false)
                ))
        );

       this.updateCurrentAccount();

        this.employeeService.getCurrentEmployee().subscribe(
            (employee: EmployeeModel) => this.currentEmployee = employee, error2 => console.log(error2));

        this.contactsService.getMyContacts()
            .subscribe((contact: ContactModel) => this.updateContactsField(contact));
    }

    private updateCurrentAccount(cache: boolean = true) {
        this.accountService
            .getCurrentAccount(cache)
            .subscribe((account: SimpleAccountModel) => {
                this.currentAccount = account;

                this.formAccount.controls['chatName'].setValue(account.chatName);
                this.formAccount.controls['login'].setValue(account.username);
                this.formAccount.controls['email'].setValue(account.email);

                this.formContact.get('email').disable(); // TODO: add role
                this.formContact.get('type').disable(); // TODO: add role

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
        currentAccountToSend.access = this.currentAccount.access;
        currentAccountToSend.employeeId = this.currentAccount.employeeId;
        currentAccountToSend.id = this.currentAccount.id;

        this.httpClient.post(this.updateAccountUrl, currentAccountToSend)
            .subscribe(
                (result: any) => {
                    this.successChangeAccount = true;

                    if (currentAccountToSend.username !== this.currentAccount.username) {
                        this.needLogout = true;
                        setTimeout(() => this.authService.logout(true).subscribe(), 2000);
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
            .pipe(finalize(() => {
                this.changePassForm.reset();
            }))
            .subscribe(
                (result: any) => {
                    this.successChangePassword = true;

                    setTimeout(() => {
                        if (this.openedChangePasswordModal) {
                            this.openedChangePasswordModal.close();
                        }

                        this.authService.logout(true).subscribe();
                    }, 2000)
                },
                (error: HttpErrorResponse) => {
                    this.serverErrorForChangePassword = error.error;
                });
    }

    submitContactsForm() {
        const contactRequestModel: ContactRequestModel = new ContactRequestModel();
        contactRequestModel.email =  this.formContact.value.email;
        contactRequestModel.mobilePhoneNumber = this.formContact.value.mobilePhoneNumber;
        contactRequestModel.workPhoneNumber = this.formContact.value.workPhoneNumber;
        contactRequestModel.type = this.formContact.value.type;
        contactRequestModel.pager = this.formContact.value.pager;

        if (this.formContact.value.placement) {
            contactRequestModel.placementId = this.formContact.value.placement.id;
        }

        this.userService.updateMyContacts(contactRequestModel)
            .subscribe((contact: ContactModel) => {
                this.updateContactsField(contact);

                const toast: Toast = {
                    type: 'success',
                    title: 'Успех',
                    body: 'Контакты сохранились',
                };

                this.toasterService.popAsync(toast);

                }, error2 => {
                    const toast: Toast = {
                        type: 'error',
                        title: 'Ошибка',
                        body: error2.error.message,
                    };

                    this.toasterService.popAsync(toast);
                }
            );
    }


    private updateContactsField(contact: ContactModel) {
        this.currentContacts = contact;

        this.formContact.controls['workPhoneNumber'].setValue(contact.workPhoneNumber);
        this.formContact.controls['mobilePhoneNumber'].setValue(contact.mobilePhoneNumber);
        this.formContact.controls['pager'].setValue(contact.pager);
        this.formContact.controls['placement'].setValue(contact.placement);
        this.formContact.controls['type'].setValue(ContactTypeModel[contact.type]);
        this.formContact.controls['email'].setValue(contact.email);
    }
}


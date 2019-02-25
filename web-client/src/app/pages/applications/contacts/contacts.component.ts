import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {NbMenuItem, NbMenuService} from '@nebular/theme';
import {ContactsService} from '../../../@core/services/contacts.service';
import {DepartmentModel} from '../../../@core/models/department.model';
import {ActivatedRoute, Router} from '@angular/router';
import {ContactsDataSource} from './contacts-data-source';
import {Cell} from 'ng2-smart-table';
import {ContactTypeModel} from '../../../@core/models/contact-type.model';
import {NbMenuBag} from '@nebular/theme/components/menu/menu.service';
import {Column} from 'ng2-smart-table/lib/data-set/column';
import {ContactModel} from '../../../@core/models/contact.model';
import {Params} from '@angular/router/src/shared';
import {SimpleAccountModel} from '../../../@core/models/simple-account.model';
import {AccountService} from '../../../@core/services/account.service';
import {CustomTableComponent} from '../../../@theme/components';
import {DepartmentEditComponent} from '../../../@theme/components/table/department-edit.component';
import {PostEditComponent} from '../../../@theme/components/table/post-edit.component';
import {PlacementEditComponent} from '../../../@theme/components/table/placement-edit.component';
import {TableEventEditModel} from '../../../@theme/components/table/table-event-edit.model';
import {ContactRequestModel} from '../../../@core/models/contact-request.model';
import {CustomActionEventModel} from '../../../@theme/components/table/custom-action-event.model';
import {SmartTableService} from '../../../@core/services/smart-table.service';
import {environment} from "../../../../environments/environment";
import {Subject} from "rxjs";
import {takeUntil} from "rxjs/operators";

@Component({
    selector: 'contacts',
    styleUrls: ['./contacts.component.scss'],
    templateUrl: `./contacts.component.html`
})
export class ContactsComponent implements OnInit, OnDestroy {
    private static currentAccount: SimpleAccountModel;

    private readonly destroySubjects: Subject<any> = new Subject<any>();
    private departmentId: number = 0;

    private contactType: ContactTypeModel;

    @ViewChild('contactsTable')
    contactsTable: CustomTableComponent;
    menu: NbMenuItem[] = [];
    cardTitle: string = 'Список контактов';
    searchValue: string = 'Список контактов';
    contactsLoading: boolean = true;
    contactsSource: ContactsDataSource;
    contactsColumn: Column[] = [];

    constructor(private contactsService: ContactsService,
                private nbMenuService: NbMenuService,
                private accountService: AccountService,
                private router: Router,
                private activatedRoute: ActivatedRoute) {
        this.contactsSource = new ContactsDataSource(this.contactsService);
        this.contactsSource.onLoading().subscribe(load => this.contactsLoading = load);

        this.activatedRoute.queryParams
            .pipe(takeUntil(this.destroySubjects))
            .subscribe(params => {
                const contactTypeName: string = params['contactType'];

                this.departmentId = params['depId'] ? params['depId'] : 0;
                this.searchValue = params['search'] ? params['search'] : null;
                this.contactType = contactTypeName ? ContactTypeModel[contactTypeName.toUpperCase()] : undefined;

                if (this.searchValue && this.searchValue.trim().length > 2) {
                    this.contactsSource.setSearch(this.searchValue);
                } else if (this.departmentId && this.departmentId > 0) {
                    this.contactsSource.setDepAndType(this.departmentId, this.contactType);
                }
            });
    }

    ngOnDestroy(): void {
        console.log(this.destroySubjects.observers.length);
        this.destroySubjects.next(true);
        this.destroySubjects.complete();
    }

    ngOnInit() {
        this.contactsService.getDepartments()
            .pipe(takeUntil(this.destroySubjects))
            .subscribe((departments: DepartmentModel[]) => {
                for (const dep of departments) {
                    const departmentMenuItem: NbMenuItem = new NbMenuItem();
                    departmentMenuItem.title = `${dep.code} - ${dep.abbreviatedName}`;
                    departmentMenuItem.expanded = false;
                    departmentMenuItem.children = [];

                    const departmentMainMenuItem: NbMenuItem = new NbMenuItem();
                    departmentMainMenuItem.title = `Основной список`;
                    departmentMainMenuItem.selected = false;
                    departmentMainMenuItem.queryParams = {
                        depId: dep.id,
                        contactType: 'main'
                    };

                    const departmentOtherMenuItem: NbMenuItem = new NbMenuItem();
                    departmentOtherMenuItem.title = `Дополнительный список`;
                    departmentOtherMenuItem.selected = false;
                    departmentOtherMenuItem.queryParams = {
                        depId: dep.id,
                        contactType: 'other'
                    };


                    if (this.departmentId && Number(this.departmentId) === Number(dep.id)) {
                        departmentMenuItem.expanded = true;
                        if (this.contactType === ContactTypeModel.MAIN) {
                            departmentMainMenuItem.selected = true;
                            departmentMainMenuItem.link = '/pages/app/contacts';
                        } else {
                            departmentOtherMenuItem.selected = true;
                            departmentOtherMenuItem.link = '/pages/app/contacts';
                        }
                    }

                    departmentMainMenuItem.parent = departmentMenuItem;
                    departmentOtherMenuItem.parent = departmentMenuItem;

                    departmentMenuItem.children.push(departmentMainMenuItem, departmentOtherMenuItem);

                    this.menu.push(departmentMenuItem);
                }

                if (this.departmentId < 1 && this.searchValue === null) {
                    this.navigateToFirstItem();
                }
            });

        this.accountService.getCurrentAccount()
            .pipe(takeUntil(this.destroySubjects))
            .subscribe((account: SimpleAccountModel) => {
                ContactsComponent.currentAccount = account;

                // if (account && (account.accessOit || account.accessOk)) { TODO add role
                //     this.contactsTable.table.initGrid();
                // }
            });

        this.initColumns();
    }

    initColumns(): void {
        this.nbMenuService.onItemClick()
            .pipe(takeUntil(this.destroySubjects))
            .subscribe((menuBag: NbMenuBag) => this.navigateToMenuItem(menuBag.item));

        const photoColumn: Column = new Column('photo', {
            title: 'Фото',
            type: 'html',
            editable: false,
            addable: false,
            sort: false,
            filter: false,
            valuePrepareFunction(a: any, value: ContactModel, cell: Cell) {
                return value.photo
                    ? `<img src="${environment.publicServerUrl + value.photo}" 
                            height="${ 250 / 2 }" width="${200 / 2}" alt="${value.initials}">`
                    : `<img src="/assets/images/no_photo.jpg" 
                            height="${ 250 / 2 }" width="${290 / 2}" alt="${value.initials}">`;
            }
        }, undefined);

        const initialsColumn: Column = new Column('initials', {
            title: 'ФИО',
            type: 'string',
            editable: false,
            addable: false,
            sort: false,
            filter: false,
        }, undefined);


        const postColumn: Column = new Column('post', {
            title: 'Должность',
            type: 'string',
            editable: false,
            addable: false,
            sort: false,
            filter: false,
            editor: {
                type: 'custom',
                component: PostEditComponent,
            },
            valuePrepareFunction(a: any, value: ContactModel, cell: Cell) {
                return value.post ? value.post.abbreviatedName : '';
            },
        }, undefined);

        const departmentColumn: Column = new Column('department', {
            title: 'Подразделние',
            type: 'string',
            editable: false,
            addable: false,
            sort: false,
            filter: false,
            editor: {
                type: 'custom',
                component: DepartmentEditComponent,
            },
            valuePrepareFunction(a: any, value: ContactModel, cell: Cell) {
                return value.department ? value.department.abbreviatedName : '';
            }
        }, undefined);

        const workPhoneNumberColumn: Column = new Column('workPhoneNumber', {
            title: 'Рабочий телефон',
            type: 'string',
            sort: false,
            filter: false,
        }, undefined);

        const mobilePhoneNumberColumn: Column = new Column('mobilePhoneNumber', {
            title: 'Мобильный телефон',
            type: 'string',
            sort: false,
            filter: false,
        }, undefined);

        const pagerColumn: Column = new Column('pager', {
            title: 'Пейджер',
            type: 'string',
            sort: false,
            filter: false,
        }, undefined);

        const emailColumn: Column = new Column('email', {
            title: 'Email',
            type: 'string',
            sort: false,
            filter: false,
        }, undefined);

        const placementColumn: Column = new Column('placement', {
            title: 'Помещение',
            type: 'string',
            sort: false,
            filter: false,
            editor: {
                type: 'custom',
                component: PlacementEditComponent,
            },
            valuePrepareFunction(a: any, value: ContactModel, cell: Cell) {
                return value.placement ? value.placement.name : '';
            }
        }, undefined);

        this.contactsColumn.push(
            photoColumn,
            initialsColumn,
            postColumn,
            departmentColumn,
            workPhoneNumberColumn,
            mobilePhoneNumberColumn,
            pagerColumn,
            emailColumn,
            placementColumn
        );

        this.contactsTable.actionBeforeValueView = this.actionBeforeValueView;
    }

    actionBeforeValueView(event: CustomActionEventModel<ContactModel>) {
        if (event.action.name === SmartTableService.DELETE_ACTION_NAME) {
            return false;
        }

        if (event.action.name === SmartTableService.EDIT_ACTION_NAME) {
            // return ContactsComponent.currentAccount TODO add role
            //     ? ContactsComponent.currentAccount.accessOit || ContactsComponent.currentAccount.accessOk
            //     : false;

            return false;
        }

        return true;
    }

    contactsEdit(event: TableEventEditModel<ContactModel>) {
        const contactRequestModel: ContactRequestModel = new ContactRequestModel();
        contactRequestModel.email = event.newData.email;
        contactRequestModel.mobilePhoneNumber = event.newData.mobilePhoneNumber;
        contactRequestModel.workPhoneNumber = event.newData.workPhoneNumber;
        contactRequestModel.type = event.newData.type;
        contactRequestModel.pager = event.newData.pager;

        if (event.newData.placement) {
            contactRequestModel.placementId = event.newData.placement.id;
        }

        this.contactsService.updateContact(event.data.employeeId, contactRequestModel)
            .subscribe((contact: ContactModel) => event.confirm.resolve(event.newData, contact),
                error2 => event.confirm.reject({}));
    }

    navigateToFirstItem(): void {
        if (this.menu.length > 0) {
            const firstElement: NbMenuItem = this.menu[0];

            if (firstElement.children.length > 0) {
                this.navigateToMenuItem(firstElement.children[0]);
            }
        }
    }

    navigateToMenuItem(menuItem: NbMenuItem) {
        if (menuItem.parent && this.menu.includes(menuItem.parent)) {
            this.nbMenuService
                .getSelectedItem('departments-list')
                .pipe(takeUntil(this.destroySubjects))
                .subscribe((menuBag: NbMenuBag) => {
                    if (menuBag && menuBag.item) {
                        menuBag.item.selected = false;
                        menuBag.item.link = null;
                    }
                });

            menuItem.link = '/pages/app/contacts';

            return this.router.navigate(['/pages/app/contacts'], { queryParams: menuItem.queryParams} );
        }
    }

    findContacts(): void {
        if (this.searchValue && this.searchValue.trim().length > 2) {
            this.nbMenuService.collapseAll('departments-list');

            const param: Params = {
                search: this.searchValue
            };

            this.router.navigate(['/pages/app/contacts'], { queryParams: param} );
        }
    }
}

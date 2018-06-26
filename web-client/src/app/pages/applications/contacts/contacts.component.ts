import {Component, OnDestroy, OnInit} from '@angular/core';
import {NbMenuItem, NbMenuService} from '@nebular/theme';
import {ContactsService} from './contacts.service';
import {DepartmentModel} from '../../../@core/models/department.model';
import {ActivatedRoute, Router} from '@angular/router';
import {ContactsDataSource} from "./contacts-data-source";
import {Subscription} from "rxjs/Rx";
import {Cell} from "ng2-smart-table";
import {ContactTypeModel} from "./contact-type.model";
import {PlacementModel} from "../../../@core/models/placement.model";
import {NbMenuBag} from "@nebular/theme/components/menu/menu.service";
import {Column} from "ng2-smart-table/lib/data-set/column";
import {ContactModel} from "./contact.model";
import {Params} from "@angular/router/src/shared";

@Component({
    selector: 'contacts',
    styleUrls: ['./contacts.component.scss'],
    templateUrl: `./contacts.component.html`
})
export class ContactsComponent implements OnInit, OnDestroy {
    private sub: Subscription;
    private departmentId: number = 0;
    private contactType: ContactTypeModel;

    menu: NbMenuItem[] = [];
    cardTitle: string = 'Список контактов';
    searchValue: string = 'Список контактов';
    contactsLoading: boolean = true;
    contactsSource: ContactsDataSource;
    contactsColumn: Column[] = [];

    constructor(private contactsService: ContactsService,
                private nbMenuService: NbMenuService,
                private router: Router,
                private activatedRoute: ActivatedRoute) {
        this.contactsSource = new ContactsDataSource(this.contactsService);
        this.contactsSource.onLoading().subscribe(load => this.contactsLoading = load);

        this.sub = this.activatedRoute.queryParams.subscribe(params => {
            this.departmentId = params['depId'] ? params['depId'] : 0;
            this.contactType = params['contactType'] ? params['contactType'].toUpperCase() : null;
            this.searchValue = params['search'] ? params['search'] : null;

            if (this.searchValue && this.searchValue.trim().length > 2) {
                this.contactsSource.setSearch(this.searchValue);
            } else if (this.departmentId && this.departmentId > 0) {
                this.contactsSource.setDepAndType(this.departmentId, this.contactType);
            }
        });
    }

    ngOnDestroy(): void {
        if (this.sub) {
            this.sub.unsubscribe();
        }
    }

    ngOnInit() {
        this.contactsService.getDepartments()
            .subscribe((departaments: DepartmentModel[]) => {
                for (const dep of departaments) {
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
                        } else {
                            departmentOtherMenuItem.selected = true;
                        }
                    }

                    departmentMainMenuItem.parent = departmentMenuItem;
                    departmentOtherMenuItem.parent = departmentMenuItem;

                    departmentMenuItem.children.push(departmentMainMenuItem, departmentOtherMenuItem);

                    this.menu.push(departmentMenuItem);
                }

                if (this.departmentId === 0 && this.searchValue === null) {
                    this.navigateToFirstItem();
                }
            });

        this.initColumns();
    }

    initColumns(): void {
        this.nbMenuService.onItemClick().subscribe((menuBag: NbMenuBag) => {
            this.navigateToMenuItem(menuBag.item);
        });

        const photoColumn: Column = new Column('photo', {
            title: 'Фото',
            type: 'html',
            editable: false,
            addable: false,
            sort: false,
            filter: false,
            valuePrepareFunction(a: any, value: ContactModel, cell: Cell) {
                return value.photo
                    ? `<img src="${value.photo}" height="${ 250 / 2 }" width="${290 / 2}" alt="${value.initials}">`
                    : `<img src="/assets/images/no_photo.jpg" height="${ 250 / 2 }" width="${290 / 2}" alt="${value.initials}">`;
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
            valuePrepareFunction(a: any, value: ContactModel, cell: Cell) {
                return value.post ? value.post.abbreviatedName : '';
            }
        }, undefined);

        const departmentColumn: Column = new Column('department', {
            title: 'Подразделние',
            type: 'string',
            editable: false,
            addable: false,
            sort: false,
            filter: false,
            valuePrepareFunction(a: any, value: ContactModel, cell: Cell) {
                return value.department ? value.department.abbreviatedName : '';
            }
        }, undefined);

        const workPhoneNumberColumn: Column = new Column('workPhoneNumber', {
            title: 'Рабочий телефон',
            type: 'string',
            editable: false,
            addable: false,
            sort: false,
            filter: false,
        }, undefined);

        const mobilePhoneNumberColumn: Column = new Column('mobilePhoneNumber', {
            title: 'Мобильный телефон',
            type: 'string',
            editable: false,
            addable: false,
            sort: false,
            filter: false,
        }, undefined);

        const pagerColumn: Column = new Column('pager', {
            title: 'Пейджер',
            type: 'string',
            editable: false,
            addable: false,
            sort: false,
            filter: false,
        }, undefined);

        const emailColumn: Column = new Column('email', {
            title: 'Email',
            type: 'string',
            editable: false,
            addable: false,
            sort: false,
            filter: false,
        }, undefined);

        const placementColumn: Column = new Column('placement', {
            title: 'Помещение',
            type: 'string',
            editable: false,
            addable: false,
            sort: false,
            filter: false,
            valuePrepareFunction(a: any, value: PlacementModel, cell: Cell) {
                return value ? value.name : '';
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
        this.nbMenuService.getSelectedItem('departments-list').subscribe((menuBag: NbMenuBag) => {
            if (menuBag && menuBag.item) {
                menuBag.item.selected = false;
                menuBag.item.link = null;
            }
        });

        menuItem.link = '/pages/app/contacts';

        return this.router.navigate(['/pages/app/contacts'], { queryParams: menuItem.queryParams} );
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

import {Component, OnDestroy, OnInit} from '@angular/core';
import {ContactsService} from '../contacts.service';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs/Rx';
import {ContactsDataSource} from './contacts-data-source';
import {ContactTypeModel} from './contact-type.model';
import {Column} from 'ng2-smart-table/lib/data-set/column';
import {Cell} from "ng2-smart-table";
import {ContactModel} from "./contact.model";
import {PlacementModel} from "../../../../@core/models/placement.model";

@Component({
    selector: 'contacts-detailed',
    styleUrls: ['./contacts-detailed.component.scss'],
    templateUrl: './contacts-detailed.component.html'
})
export class ContactsDetailedComponent implements OnInit, OnDestroy {
    private sub: Subscription;
    private departmentId: number = 0;
    private contactType: ContactTypeModel;

    contactsLoading: boolean = true;
    contactsSource: ContactsDataSource;
    contactsColumn: Column[] = [];

    constructor(private contactsService: ContactsService,
                private activatedRoute: ActivatedRoute) {
        this.contactsSource = new ContactsDataSource(this.contactsService);
        this.contactsSource.onLoading().subscribe(load => this.contactsLoading = load);

        this.sub = this.activatedRoute.params.subscribe(params => {
            this.departmentId = params['id'];
            this.contactType = params['type'].toUpperCase();

            this.contactsSource.setDepAndType(this.departmentId, this.contactType);
        });
    }

    ngOnInit(): void {
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
        }, undefined);

        const mobilePhoneNumberColumn: Column = new Column('mobilePhoneNumber', {
            title: 'Мобильный телефон',
            type: 'string',
        }, undefined);

        const pagerColumn: Column = new Column('pager', {
            title: 'Пейджер',
            type: 'string',
        }, undefined);

        const emailColumn: Column = new Column('email', {
            title: 'E-mail',
            type: 'string',
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

    ngOnDestroy(): void {
        if (this.sub) {
            this.sub.unsubscribe();
        }
    }

}

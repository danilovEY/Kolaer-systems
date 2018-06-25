import {Component, OnInit} from '@angular/core';
import {NbMenuItem} from '@nebular/theme';
import {ContactsService} from './contacts.service';
import {DepartmentModel} from '../../../@core/models/department.model';
import {Router} from '@angular/router';

@Component({
    selector: 'contacts',
    styleUrls: ['./contacts.component.scss'],
    templateUrl: `./contacts.component.html`
})
export class ContactsComponent implements OnInit {
    menu: NbMenuItem[] = [];

    constructor(private contactsService: ContactsService,
                private router: Router) {
    }

    ngOnInit() {
        this.contactsService.getDepartments()
            .subscribe((departaments: DepartmentModel[]) => {
                for (const dep of departaments) {
                    const departmentMenuItem: NbMenuItem = new NbMenuItem();
                    departmentMenuItem.title = `${dep.code} - ${dep.abbreviatedName}`;
                    departmentMenuItem.children = [];

                    const departmentMainMenuItem: NbMenuItem = new NbMenuItem();
                    departmentMainMenuItem.title = `Основной список`;
                    departmentMainMenuItem.link = `/pages/app/contacts/${dep.id}/main`;

                    const departmentOtherMenuItem: NbMenuItem = new NbMenuItem();
                    departmentOtherMenuItem.title = `Дополнительный список`;
                    departmentOtherMenuItem.link = `/pages/app/contacts/${dep.id}/other`;

                    departmentMenuItem.children.push(departmentMainMenuItem, departmentOtherMenuItem);

                    this.menu.push(departmentMenuItem);
                }

                // if (this.menu.length > 0) {
                //     const firstElement: NbMenuItem = this.menu[0];
                //     firstElement.expanded = true;
                //
                //     if (firstElement.children.length > 0) {
                //         const mainOfFirstElements: NbMenuItem = firstElement.children[0];
                //         mainOfFirstElements.selected = true;
                //         this.router.navigate([mainOfFirstElements.link]);
                //     }
                // }
            });
    }
}

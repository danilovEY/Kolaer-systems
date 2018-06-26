import {CustomDataSource} from '../../../@core/models/custom.data-source';
import {ContactModel} from './contact.model';
import {ContactTypeModel} from './contact-type.model';
import {ContactsService} from './contacts.service';
import {Page} from '../../../@core/models/page.model';

export class ContactsDataSource extends CustomDataSource<ContactModel> {
    constructor(private contactsService: ContactsService,
                private departmentId: number = 0,
                private contactType: ContactTypeModel = ContactTypeModel.OTHER,
                private search?: string) {
        super();
    }

    loadElements(page: number, pageSize: number): Promise<ContactModel[]> {
        if (this.search) {
            return this.contactsService.getContactsBySearch(page, pageSize, this.search)
                .toPromise()
                .then((response: Page<ContactModel>) => this.setDataPage(response));
        }

        if (this.departmentId < 1) {
           return this.empty();
        }

        return this.contactsService.getContactsByDepartment(page, pageSize, this.departmentId, this.contactType)
            .toPromise()
            .then((response: Page<ContactModel>) => this.setDataPage(response));
    }
    
    setDepAndType(depId: number, contactType: ContactTypeModel): void {
        this.departmentId = depId;
        this.contactType = ContactTypeModel[contactType];

        this.search = null;

        this.refresh();
    }

    setSearch(search: string) {
        this.departmentId = 0;
        this.contactType = ContactTypeModel.OTHER;

        this.search = search;

        this.refresh();
    }
}

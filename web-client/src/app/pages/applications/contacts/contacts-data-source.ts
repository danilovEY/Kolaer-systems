import {CustomDataSource} from '../../../@core/models/custom.data-source';
import {ContactModel} from '../../../@core/models/contact.model';
import {ContactTypeModel} from '../../../@core/models/contact-type.model';
import {ContactsService} from '../../../@core/services/contacts.service';
import {Page} from '../../../@core/models/page.model';

export class ContactsDataSource extends CustomDataSource<ContactModel> {
    private initData: boolean = true;

    constructor(private contactsService: ContactsService,
                private departmentId: number = 0,
                private contactType: ContactTypeModel = ContactTypeModel.MAIN,
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
           return new Promise(resolve => resolve(new Page(this.data)))
               .then((newData: Page<ContactModel>) => this.setDataPage(newData));
        }

        return this.contactsService.getContactsByDepartment(page, pageSize, this.departmentId, this.contactType)
            .toPromise()
            .then((response: Page<ContactModel>) => this.setDataPage(response));
    }
    
    setDepAndType(depId: number, contactType: ContactTypeModel): void {
        this.departmentId = depId;
        this.contactType = contactType;

        this.search = null;

        if (!this.initData) {
            this.refreshFromServer();
        } else {
            this.refreshFromServer();
            this.initData = false;
        }
    }

    setSearch(search: string) {
        this.departmentId = 0;
        this.contactType = ContactTypeModel.OTHER;

        this.search = search;

        if (!this.initData) {
            this.refreshFromServer();
        } else {
            this.refreshFromServer();
            this.initData = false;
        }
    }
}

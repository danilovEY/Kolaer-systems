import {CustomDataSource} from '../../../../@core/models/custom.data-source';
import {ContactModel} from './contact.model';
import {ContactTypeModel} from './contact-type.model';
import {ContactsService} from '../contacts.service';
import {Page} from '../../../../@core/models/page.model';

export class ContactsDataSource extends CustomDataSource<ContactModel> {
    constructor(private contactsService: ContactsService,
                private departmentId: number = 0,
                private contactType: ContactTypeModel = ContactTypeModel.OTHER) {
        super();
    }

    loadElements(page: number, pageSize: number): Promise<ContactModel[]> {
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

        this.refresh();
    }
    
}

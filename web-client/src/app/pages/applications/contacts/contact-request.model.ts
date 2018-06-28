import {ContactTypeModel} from './contact-type.model';

export class ContactRequestModel {
    public workPhoneNumber: string;
    public mobilePhoneNumber: string;
    public pager: string;
    public email: string;
    public type: ContactTypeModel;
    public placementId: number;
}

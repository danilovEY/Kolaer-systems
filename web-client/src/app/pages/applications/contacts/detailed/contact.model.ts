import {PostModel} from '../../../../@core/models/post.model';
import {DepartmentModel} from '../../../../@core/models/department.model';
import {ContactTypeModel} from './contact-type.model';
import {PlacementModel} from '../../../../@core/models/placement.model';

export class ContactModel {
    public employeeId: number;
    public initials: string;
    public photo: string;
    public post: PostModel;
    public department: DepartmentModel;
    public workPhoneNumber: string;
    public mobilePhoneNumber: string;
    public pager: string;
    public email: string;
    public type: ContactTypeModel;
    public placement: PlacementModel;
}

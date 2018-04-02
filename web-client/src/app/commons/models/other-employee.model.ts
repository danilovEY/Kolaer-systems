import {Category} from './category.enum';
import {Gender} from './gender.enum';
import {BaseModel} from './base.model';

export class OtherEmployeeModel extends BaseModel {
    birthday: Date;
    category: Category;
    department: string;
    email: string;
    firstName: string;
    secondName: string;
    thirdName: string;
    initials: string;
    photo: string;
    mobilePhone: string;
    workPhoneNumber: string;
    organization: string;
    gender: Gender;
    post: string;
}

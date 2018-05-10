import {Category} from './category.enum';
import {DepartmentModel} from './department.model';
import {Gender} from './gender.enum';
import {PostModel} from './post.model';
import {BaseModel} from './base.model';

export class EmployeeModel extends BaseModel {
    birthday: Date;
    category: Category;
    department: DepartmentModel;
    dismissalDate: Date;
    employmentDate: Date;
    email: string;
    firstName: string;
    secondName: string;
    thirdName: string;
    initials: string;
    photo: string;
    workPhoneNumber: string;
    homePhoneNumber: string;
    personnelNumber: number;
    gender: Gender;
    post: PostModel;
}

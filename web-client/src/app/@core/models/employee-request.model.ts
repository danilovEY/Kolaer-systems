import {Category} from './category.enum';
import {Gender} from './gender.enum';

export class EmployeeRequestModel {
    birthday: string;
    category: Category;
    departmentId: number;
    employmentDate: Date;
    email: string;
    firstName: string;
    secondName: string;
    thirdName: string;
    workPhoneNumber: string;
    homePhoneNumber: string;
    personnelNumber: number;
    gender: Gender;
    postId: number;
}

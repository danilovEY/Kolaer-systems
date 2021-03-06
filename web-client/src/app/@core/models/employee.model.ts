import {Category} from './category.enum';
import {DepartmentModel} from './department/department.model';
import {Gender} from './gender.enum';
import {PostModel} from './post.model';
import {BaseModel} from './base.model';
import {TypeWorkModel} from './type-work.model';

export class EmployeeModel extends BaseModel {
    birthday: Date;
    category: Category;
    department: DepartmentModel;
    dismissalDate: Date;
    employmentDate: Date;
    firstName: string;
    secondName: string;
    thirdName: string;
    initials: string;
    photo: string;
    personnelNumber: number;
    gender: Gender;
    post: PostModel;
    harmfulness: boolean;
    typeWork: TypeWorkModel;
    contractNumber: string;
}

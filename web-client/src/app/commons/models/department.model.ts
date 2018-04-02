import {BaseModel} from './base.model';

export class DepartmentModel extends BaseModel {
    abbreviatedName: string;
    name: string;
    chefId: number;
}

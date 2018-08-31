import {BaseModel} from '../../../../@core/models/base.model';

export class VacationBalanceModel extends BaseModel {
    public employeeId: number;
    public nextYearBalance: number;
    public currentYearBalance: number;
    public prevYearBalance: number;
}

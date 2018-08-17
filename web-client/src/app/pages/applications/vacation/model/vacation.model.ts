import {BaseModel} from '../../../../@core/models/base.model';
import {VacationTypeEnum} from './vacation-type-enum.model';

export class VacationModel extends BaseModel {
    public employeeId: number;
    public note: string;
    public vacationFrom: Date;
    public vacationTo: Date;
    public vacationDays: number;
    public vacationType: VacationTypeEnum;
}

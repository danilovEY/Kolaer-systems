import {BaseModel} from '../../../../@core/models/base.model';
import {VacationPeriodStatusEnum} from './vacation-period-status.enum';

export class VacationPeriodModel extends BaseModel {
    public year: number;
    public status: VacationPeriodStatusEnum;
}

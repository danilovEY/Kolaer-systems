import {VacationReportDistributeLineModel} from './vacation-report-distribute-line.model';
import {VacationReportPipeModel} from './vacation-report-pipe.model';

export class VacationReportDistributeModel {
    public maxSize: number;
    public lineValues: VacationReportDistributeLineModel[];
    public pipeValues: VacationReportPipeModel[];
}

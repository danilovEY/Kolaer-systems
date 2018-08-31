import {VacationReportDistributeLineModel} from './vacation-report-distribute-line.model';
import {VacationReportDistributePipeModel} from './vacation-report-distribute-pipe.model';

export class VacationReportDistributeModel {
    public maxSize: number;
    public lineValues: VacationReportDistributeLineModel[];
    public pipeValues: VacationReportDistributePipeModel[];
}

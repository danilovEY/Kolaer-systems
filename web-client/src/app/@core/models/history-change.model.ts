import {SimpleAccountModel} from './simple-account.model';
import {HistoryChangeEventEnum} from './history-change-event.enum';

export class HistoryChangeModel {
	eventDate: Date;
	valueOld: string;
	valueNew: string;
	event: HistoryChangeEventEnum;
	account: SimpleAccountModel;
}

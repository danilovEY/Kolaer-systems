import {CustomDataSource} from '../../../@core/models/custom.data-source';
import {TicketsService} from './tickets.service';
import {Page} from '../../../@core/models/page.model';
import {TicketModel} from './ticket.model';

export class TicketsDataSource extends CustomDataSource<TicketModel> {

    constructor(private registerId: number, private ticketsService: TicketsService) {
        super();
    }

    loadElements(page: number, pageSize: number): Promise<TicketModel[]> {
        return this.ticketsService.getAllTicketsByRegisterId(this.registerId, page, pageSize)
            .toPromise()
            .then((response: Page<TicketModel>) => this.setData(response));
    }

}

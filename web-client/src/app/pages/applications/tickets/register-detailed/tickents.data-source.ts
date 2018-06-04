import {CustomDataSource} from '../../../../@core/models/custom.data-source';
import {TicketsService} from '../tickets.service';
import {Page} from '../../../../@core/models/page.model';
import {TicketModel} from '../ticket.model';
import {TicketsFilterModel} from '../tickets-filter.model';
import {TicketsSortModel} from "../tickets-sort.model";

export class TicketsDataSource extends CustomDataSource<TicketModel> {

    constructor(private registerId: number, private ticketsService: TicketsService) {
        super();
    }

    loadElements(page: number, pageSize: number): Promise<TicketModel[]> {
        const ticketsFilter: TicketsFilterModel = this.getFilterModel(new TicketsFilterModel());
        const ticketsSort: TicketsSortModel = this.getSortModel(new TicketsSortModel());

        return this.ticketsService.getAllTicketsByRegisterId(this.registerId, ticketsSort, ticketsFilter, page, pageSize)
            .toPromise()
            .then((response: Page<TicketModel>) => this.setData(response));
    }

}

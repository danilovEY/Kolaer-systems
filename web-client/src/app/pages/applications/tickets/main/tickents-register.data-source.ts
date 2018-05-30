import {CustomDataSource} from '../../../../@core/models/custom.data-source';
import {TicketRegisterModel} from './ticket-register.model';
import {TicketsService} from '../tickets.service';
import {Page} from '../../../../@core/models/page.model';

export class TicketsRegisterDataSource extends CustomDataSource<TicketRegisterModel> {

    constructor(private ticketsService: TicketsService) {
        super();
    }

    loadElements(page: number, pageSize: number): Promise<TicketRegisterModel[]> {
        return this.ticketsService.getAllTicketRegisters(page, pageSize)
            .toPromise()
            .then((response: Page<TicketRegisterModel>) => this.setData(response));
    }

}

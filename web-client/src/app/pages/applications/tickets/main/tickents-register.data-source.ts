import {CustomDataSource} from '../../../../@core/models/custom.data-source';
import {TicketRegisterModel} from './ticket-register.model';
import {TicketsService} from '../tickets.service';
import {Page} from '../../../../@core/models/page.model';
import {RegisterTicketsFilterModel} from "../register-tickets-filter.model";
import {RegisterTicketsSortModel} from "../register-tickets-sort.model";

export class TicketsRegisterDataSource extends CustomDataSource<TicketRegisterModel> {

    constructor(private ticketsService: TicketsService) {
        super();
    }

    loadElements(page: number, pageSize: number): Promise<TicketRegisterModel[]> {
        const registerTicketsFilterModel: RegisterTicketsFilterModel =
            this.getFilterModel(new RegisterTicketsFilterModel());

        const registerTicketsSortModel: RegisterTicketsSortModel =
            this.getSortModel(new RegisterTicketsSortModel());

        return this.ticketsService.getAllTicketRegisters(registerTicketsSortModel, registerTicketsFilterModel,
            page, pageSize)
            .toPromise()
            .then((response: Page<TicketRegisterModel>) => this.setData(response));
    }

}

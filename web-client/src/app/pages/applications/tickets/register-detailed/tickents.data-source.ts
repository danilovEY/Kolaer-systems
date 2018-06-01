import {CustomDataSource} from '../../../../@core/models/custom.data-source';
import {TicketsService} from '../tickets.service';
import {Page} from '../../../../@core/models/page.model';
import {TicketModel} from '../ticket.model';
import {ColumnSort} from '../../../../@theme/components/table/column-sort';
import {Utils} from '../../../../@core/utils/utils';
import {TableFilters} from '../../../../@theme/components/table/table-filter';
import {TicketsFilterModel} from '../tickets-filter.model';
import {TypeOperationEnum} from '../main/type-operation.enum';
import {TicketsSortModel} from "../tickets-sort.model";

export class TicketsDataSource extends CustomDataSource<TicketModel> {

    constructor(private registerId: number, private ticketsService: TicketsService) {
        super();
    }

    loadElements(page: number, pageSize: number): Promise<TicketModel[]> {
        const tableFilters: TableFilters = this.getFilter();
        const columnSorts: ColumnSort[] = this.getSort();

        let ticketsSort: TicketsSortModel = null;
        let ticketsFilter: TicketsFilterModel = null;

        if (columnSorts.length > 0) {
            const columnSort: ColumnSort = columnSorts[0];
            ticketsSort = new TicketsSortModel();

            if (columnSort.field === 'employee.post.name') {
                ticketsSort.sortEmployeePost = Utils.getSortType(columnSort.direction);
            } else if (columnSort.field === 'employee.department.name') {
                ticketsSort.sortEmployeeDepartment = Utils.getSortType(columnSort.direction);
            } else if (columnSort.field === 'employee') {
                ticketsSort.sortEmployeeInitials = Utils.getSortType(columnSort.direction);
            } else if (columnSort.field === 'count') {
                ticketsSort.sortCount = Utils.getSortType(columnSort.direction);
            } else if (columnSort.field === 'type') {
                ticketsSort.sortTypeOperation = Utils.getSortType(columnSort.direction);
            } else {
                ticketsSort.sortId = Utils.getSortType(columnSort.direction);
            }
        }

        if (tableFilters.filters.length > 0) {
            ticketsFilter = new TicketsFilterModel();
            for (const filer of tableFilters.filters) {
                if (filer.field === 'employee.post.name') {
                    ticketsFilter.filterEmployeePost = filer.search;
                } else if (filer.field === 'employee.department.name') {
                    ticketsFilter.filterEmployeeDepartment = filer.search;
                } else if (filer.field === 'employee') {
                    ticketsFilter.filterEmployeeInitials = filer.search;
                } else if (filer.field === 'count') {
                    ticketsFilter.filterCount = Number(filer.search);
                } else if (filer.field === 'type') {
                    ticketsFilter.filterTypeOperation = TypeOperationEnum[Utils.keyFromValue(TypeOperationEnum, filer.search)];
                } else {
                    ticketsFilter.filterId = Number(filer.search);
                }
            }
        }

        return this.ticketsService.getAllTicketsByRegisterId(this.registerId, ticketsSort, ticketsFilter, page, pageSize)
            .toPromise()
            .then((response: Page<TicketModel>) => this.setData(response));
    }

}

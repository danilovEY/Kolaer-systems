import {Component, OnInit} from '@angular/core';
import {ProductionCalendarDataSource} from './production-calendar.data-source';
import {ProductionCalendarService} from '../../../../@core/services/production-calendar.service';
import {Column} from 'ng2-smart-table/lib/data-set/column';
import {Utils} from '../../../../@core/utils/utils';
import {Cell} from 'ng2-smart-table';
import {TypeDay} from '../../../../@core/models/typeday.enum';
import {DateEditComponent} from '../../../../@theme/components/table/date-edit.component';
import {HolidayModel} from '../../../../@core/models/holiday.model';
import {TableEventEditModel} from '../../../../@theme/components/table/table-event-edit.model';
import {TableEventAddModel} from '../../../../@theme/components/table/table-event-add.model';
import {TableEventDeleteModel} from '../../../../@theme/components/table/table-event-delete.model';

@Component({
    selector: 'production-calendar-edit',
    styleUrls: ['./production-calendar-edit.component.scss'],
    templateUrl: './production-calendar-edit.component.html'
})
export class ProductionCalendarEditComponent implements OnInit {
    calendarLoading: boolean = true;
    calendarSource: ProductionCalendarDataSource;
    calendarColumns: Column[] = [];

    constructor(private productionCalendarService: ProductionCalendarService) {
        this.calendarSource = new ProductionCalendarDataSource(this.productionCalendarService);
        this.calendarSource.onLoading().subscribe(load => this.calendarLoading = load);
    }

    ngOnInit() {
        const nameColumn: Column = new Column('name', {
            title: 'Имя',
            type: 'string'
        }, null);

        const holidayDateColumn: Column = new Column('holidayDate', {
            title: 'Дата',
            type: 'string',
            editor: {
                type: 'custom',
                component: DateEditComponent,
            },
            valuePrepareFunction(a: any, value: HolidayModel, cell: Cell) {
                return Utils.getDateFormat(value.holidayDate);
            }
        }, null);

        const typeColumn: Column = new Column('holidayType', {
            title: 'Тип',
            type: 'string',
            filter: {
                type: 'list',
                config: {
                    selectText: 'Тип...',
                    list: [
                        {value: Utils.keyFromValue(TypeDay, TypeDay.HOLIDAY), title: TypeDay.HOLIDAY},
                        {value: Utils.keyFromValue(TypeDay, TypeDay.WORK), title: TypeDay.WORK},
                        {value: Utils.keyFromValue(TypeDay, TypeDay.SHORT), title: TypeDay.SHORT},
                    ],
                },
            },
            editor: {
                type: 'list',
                config: {
                    list: [
                        {value: Utils.keyFromValue(TypeDay, TypeDay.HOLIDAY), title: TypeDay.HOLIDAY},
                        {value: Utils.keyFromValue(TypeDay, TypeDay.WORK), title: TypeDay.WORK},
                        {value: Utils.keyFromValue(TypeDay, TypeDay.SHORT), title: TypeDay.SHORT},
                    ],
                },
            },
            valuePrepareFunction(a: any, value: HolidayModel, cell: Cell) {
                return TypeDay[value.holidayType];
            }
        }, null);

        this.calendarColumns.push(nameColumn, holidayDateColumn, typeColumn);
    }

    calendarEdit(event: TableEventEditModel<HolidayModel>) {
        this.productionCalendarService.updateHoliday(event.data.id, event.newData)
            .subscribe((response: HolidayModel) => event.confirm.resolve(event.newData, response),
                error2 => event.confirm.reject({}));
    }

    calendarCreate(event: TableEventAddModel<HolidayModel>) {
        this.productionCalendarService.createHoliday(event.newData)
            .subscribe((response: HolidayModel) => event.confirm.resolve(response),
                    error2 => event.confirm.reject({}));
    }

    calendarDelete(event: TableEventDeleteModel<HolidayModel>) {
        if (confirm(`Вы действительно хотите удалить: ${event.data.name}`)) {
            this.productionCalendarService.deleteHoliday(event.data.id)
                .subscribe(
                    response => event.confirm.resolve({}),
                    error2 => event.confirm.reject({})
                );
        } else {
            event.confirm.reject({});
        }
    }

}

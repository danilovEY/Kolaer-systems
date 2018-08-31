import {ModuleWithProviders, NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {AccountService} from '../services/account.service';
import {EmployeeService} from '../services/employee.service';
import {RouterNavigatorService} from '../services/router-navigator.service';
import {DepartmentService} from '../services/department.service';
import {PostService} from '../services/post.service';
import {ProductionCalendarService} from '../services/production-calendar.service';
import {CustomDatePickerLangService} from '../services/custom-date-picker-lang.service';
import {PlacementService} from '../services/placement.service';
import {ContactsService} from '../services/contacts.service';
import {TypeWorkService} from '../services/type-work.service';
import {SmartTableService} from '../services/smart-table.service';

const SERVICES = [
    AccountService,
    EmployeeService,
    DepartmentService,
    PostService,
    RouterNavigatorService,
    ProductionCalendarService,
    CustomDatePickerLangService,
    PlacementService,
    ContactsService,
    TypeWorkService,
    SmartTableService,
];

@NgModule({
    imports: [
        CommonModule,
    ],
    providers: [
        ...SERVICES,
    ],
})
export class DataModule {
    static forRoot(): ModuleWithProviders {
        return <ModuleWithProviders>{
            ngModule: DataModule,
            providers: [
                ...SERVICES,
            ],
        };
    }
}

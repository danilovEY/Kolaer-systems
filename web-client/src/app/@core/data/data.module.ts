import {ModuleWithProviders, NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {AccountService} from '../services/account.service';
import {EmployeeService} from '../services/employee.service';

const SERVICES = [
    AccountService,
    EmployeeService,
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

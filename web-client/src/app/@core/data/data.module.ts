import {ModuleWithProviders, NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {AccountService} from '../services/account.service';
import {EmployeeService} from '../services/employee.service';
import {RouterNavigatorService} from "../services/router-navigator.service";
import {DepartmentService} from "../services/department.service";
import {PostService} from "../services/post.service";

const SERVICES = [
    AccountService,
    EmployeeService,
    DepartmentService,
    PostService,
    RouterNavigatorService,
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

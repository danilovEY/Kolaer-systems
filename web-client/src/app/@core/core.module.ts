import {ModuleWithProviders, NgModule, Optional, SkipSelf} from '@angular/core';
import {CommonModule} from '@angular/common';
import {throwIfAlreadyLoaded} from './module-import-guard';
import {DataModule} from './data/data.module';
// import {NbAuthModule} from '@nebular/auth';

const NB_CORE_PROVIDERS = [
    ...DataModule.forRoot().providers,
    // NbSecurityModule.forRoot({
    //     accessControl: {
    //         guest: {
    //             view: '*',
    //         },
    //         user: {
    //             parent: 'guest',
    //             create: '*',
    //             edit: '*',
    //             remove: '*',
    //         },
    //     },
    // }).providers,
    // {
    //     provide: NbRoleProvider,
    //     useValue: {
    //         getRole: () => {
    //             return observableOf('guest'); // here you could provide any role based on any auth flow
    //         },
    //     },
    // }
];

@NgModule({
    imports: [
        CommonModule,
    ],
    exports: [
        // NbAuthModule,
    ],
    declarations: [],
})
export class CoreModule {
    static forRoot(): ModuleWithProviders {
        return <ModuleWithProviders>{
            ngModule: CoreModule,
            providers: [
                ...NB_CORE_PROVIDERS,
            ],
        };
    }

    constructor(@Optional() @SkipSelf() parentModule: CoreModule) {
        throwIfAlreadyLoaded(parentModule, 'CoreModule');
    }
}

import {ModuleWithProviders, NgModule, Optional, SkipSelf} from '@angular/core';
import {CommonModule} from '@angular/common';
import {NbAuthModule, NbDummyAuthProvider} from '@nebular/auth';
import {NbRoleProvider, NbSecurityModule} from '@nebular/security';
import {of as observableOf} from 'rxjs/observable/of';

import {throwIfAlreadyLoaded} from './module-import-guard';

const NB_CORE_PROVIDERS = [
    ...NbAuthModule.forRoot({
        providers: {
            email: {
                service: NbDummyAuthProvider
            }
        }
    }).providers,
    NbSecurityModule.forRoot({
        accessControl: {
            guest: {
                view: '*',
            },
            user: {
                parent: 'guest',
                create: '*',
                edit: '*',
                remove: '*',
            },
        },
    }).providers,
    {
        provide: NbRoleProvider,
        useValue: {
            getRole: () => {
                return observableOf('guest'); // here you could provide any role based on any auth flow
            },
        },
    }
];

@NgModule({
    imports: [
        CommonModule,
    ],
    exports: [
        NbAuthModule,
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

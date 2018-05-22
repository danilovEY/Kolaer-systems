import {NgModule} from '@angular/core';
import {ThemeModule} from '../../../@theme/theme.module';
import {KolpassRouteModule} from './kolpass-route.module';
import {KolpassService} from './kolpass.service';
import {ClipboardModule} from 'ngx-clipboard';
import {RepositoryDetailedComponent} from './detailed/repository-detailed.component';
import {RepositoriesComponent} from './repositories/repositories.component';
import {KolpassComponent} from './kolpass.component';


@NgModule({
    imports: [
        KolpassRouteModule,
        ThemeModule,
        ClipboardModule,
    ],
    providers: [
        KolpassService
    ],
    declarations: [
        KolpassComponent,
        RepositoriesComponent,
        RepositoryDetailedComponent,
    ],
    exports: [
        KolpassComponent
    ]
})
export class KolpassModule {
    
}

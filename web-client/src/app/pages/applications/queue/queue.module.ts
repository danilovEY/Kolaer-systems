import {NgModule} from '@angular/core';
import {QueueComponent} from './queue.component';
import {QueueMainComponent} from './main/queue-main.component';
import {ThemeModule} from '../../../@theme/theme.module';
import {QueueRouterModule} from './queue-router.module';
import {QueueService} from './queue.service';
import {QueueRequestComponent} from './requests/queue-request.component';

@NgModule({
    imports: [
        QueueRouterModule,
        ThemeModule
    ],
    declarations: [
        QueueComponent,
        QueueMainComponent,
        QueueRequestComponent
    ],
    providers: [
        QueueService
    ]
})
export class QueueModule {

}

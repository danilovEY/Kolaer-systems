import {NgModule} from '@angular/core';
import {QueueComponent} from './queue.component';
import {QueueTargetComponent} from './target/queue-target.component';
import {ThemeModule} from '../../../@theme/theme.module';
import {QueueRouterModule} from './queue-router.module';
import {QueueService} from './queue.service';
import {QueueRequestComponent} from './requests/queue-request.component';
import {QueueMainComponent} from './main/queue-main.component';

@NgModule({
    imports: [
        QueueRouterModule,
        ThemeModule
    ],
    declarations: [
        QueueComponent,
        QueueTargetComponent,
        QueueRequestComponent,
        QueueMainComponent
    ],
    providers: [
        QueueService
    ]
})
export class QueueModule {

}

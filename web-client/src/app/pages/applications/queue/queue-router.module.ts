import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {QueueComponent} from './queue.component';
import {QueueTargetComponent} from './target/queue-target.component';
import {QueueRequestComponent} from './requests/queue-request.component';
import {QueueMainComponent} from './main/queue-main.component';

const routes: Routes = [
    {
        path: '',
        redirectTo: 'main',
        component: QueueComponent
    },
    {
        path: 'main',
        component: QueueMainComponent
    },
    {
        path: 'target',
        component: QueueTargetComponent
    },
    {
        path: 'target/:id/request',
        component: QueueRequestComponent
    },
];

@NgModule({
    imports: [
        RouterModule.forChild(routes)
    ],
    exports: [
        RouterModule
    ]
})
export class QueueRouterModule {

}

import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {QueueComponent} from './queue.component';
import {QueueMainComponent} from './main/queue-main.component';
import {QueueRequestComponent} from './requests/queue-request.component';

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
        path: ':id/request',
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

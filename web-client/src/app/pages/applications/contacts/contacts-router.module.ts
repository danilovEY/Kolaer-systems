import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ContactsComponent} from './contacts.component';
import {ContactsDetailedComponent} from './detailed/contacts-detailed.component';

const routers: Routes = [
    {
        path: '',
        component: ContactsComponent,
        children: [
            {
                path: ':id/:type',
                component: ContactsDetailedComponent
            },
        ]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routers)],
    exports: [RouterModule],
})
export class ContactsRouterModule {

}

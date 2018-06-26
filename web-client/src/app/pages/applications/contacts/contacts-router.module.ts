import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ContactsComponent} from './contacts.component';

const routers: Routes = [
    {
        path: '',
        component: ContactsComponent
    }
];

@NgModule({
    imports: [RouterModule.forChild(routers)],
    exports: [RouterModule],
})
export class ContactsRouterModule {

}

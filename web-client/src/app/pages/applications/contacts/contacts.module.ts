import {NgModule} from '@angular/core';
import {ContactsRouterModule} from './contacts-router.module';
import {ContactsComponent} from './contacts.component';
import {ThemeModule} from '../../../@theme/theme.module';

@NgModule({
    imports: [
        ContactsRouterModule,
        ThemeModule,
    ],
    declarations: [
        ContactsComponent,
    ],
    providers: [

    ]
})
export class ContactsModule {

}

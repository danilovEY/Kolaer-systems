import {NgModule} from '@angular/core';
import {ContactsRouterModule} from './contacts-router.module';
import {ContactsComponent} from './contacts.component';
import {ThemeModule} from '../../../@theme/theme.module';
import {ContactsService} from './contacts.service';

@NgModule({
    imports: [
        ContactsRouterModule,
        ThemeModule,
    ],
    declarations: [
        ContactsComponent,
    ],
    providers: [
        ContactsService,
    ]
})
export class ContactsModule {

}

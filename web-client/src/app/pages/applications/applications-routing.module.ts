import {RouterModule, Routes} from '@angular/router';
import {NgModule} from '@angular/core';
import {ApplicationsComponent} from './applications.component';
import {KolpassModule} from './kolpass/kolpass.module';
import {AuthGuardService} from '../../@core/modules/auth/auth-guard.service';


const routes: Routes = [{
    path: '',
    component: ApplicationsComponent,
    children: [
        {
            path: 'kolpass',
            loadChildren: () => KolpassModule,
            canActivate: [AuthGuardService]
        },
    ]
}];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule],
})
export class ApplicationsRoutingModule {
}

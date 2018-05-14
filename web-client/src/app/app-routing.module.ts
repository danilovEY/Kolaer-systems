import {ExtraOptions, RouterModule, Routes} from '@angular/router';
import {NgModule} from '@angular/core';
import {CustomLoginComponent, CustomLogoutComponent} from './@theme/components';

const routes: Routes = [
    {path: 'pages', loadChildren: 'app/pages/pages.module#PagesModule'},
    {
        path: 'auth',
        children: [
            {
                path: '',
                pathMatch: 'full',
                redirectTo: '/login',
            },
            {
                path: 'login',
                component: CustomLoginComponent,
            },
            {
                path: 'logout',
                component: CustomLogoutComponent,
            }
        ],
    },
    {path: '', redirectTo: 'pages', pathMatch: 'full'},
    {path: '**', redirectTo: 'pages'},
];

const config: ExtraOptions = {
    useHash: true,
};

@NgModule({
    imports: [RouterModule.forRoot(routes, config)],
    exports: [RouterModule],
})
export class AppRoutingModule {
}

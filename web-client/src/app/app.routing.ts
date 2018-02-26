import {NgModule} from '@angular/core';
import {HashLocationStrategy, LocationStrategy} from '@angular/common';
import {Routes, RouterModule} from '@angular/router';
import {NotFoundComponent} from './commons/pages/not-found/not-found.component';

const routes: Routes = [
	{path: '', redirectTo: 'home', pathMatch: 'full'},
	{ path: '**', component: NotFoundComponent }
];

@NgModule({
	imports: [
		RouterModule.forRoot(routes, {useHash: true})
	],
	providers: [
		{provide: LocationStrategy, useClass: HashLocationStrategy}
	],
	exports: [
		RouterModule
	],
})
export class AppRoutingModule {
}


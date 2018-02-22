import {NgModule} from '@angular/core';
import {HashLocationStrategy, LocationStrategy} from '@angular/common';
import {Routes, RouterModule} from '@angular/router';


const routes: Routes = [
	{path: '', redirectTo: 'home', pathMatch: 'full'},
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


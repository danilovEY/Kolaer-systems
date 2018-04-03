import {NgModule} from '@angular/core';
import {SidebarComponent} from './sidebar.component';
import {AppCoreModule} from '../../app-core.module';
import {RouterModule} from '@angular/router';

@NgModule({
	declarations: [
		SidebarComponent
	],
	imports: [
		AppCoreModule,
        RouterModule
	],
	exports: [
		SidebarComponent
	]
})
export class SidebarModule {
}

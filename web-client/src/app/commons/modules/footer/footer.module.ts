import {NgModule} from '@angular/core';
import {FooterComponent} from './footer.component';
import {AppCoreModule} from '../../app-core.module';

@NgModule({
	declarations: [
		FooterComponent
	],
	imports: [
		AppCoreModule
	],
	exports: [
		FooterComponent
	]
})
export class FooterModule {
}

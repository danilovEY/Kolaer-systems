import {NgModule} from '@angular/core';
import {FooterComponent} from './footer.component';
import {AppCommonModule} from '../../app-common.module';

@NgModule({
	declarations: [
		FooterComponent
	],
	imports: [
		AppCommonModule
	],
	exports: [
		FooterComponent
	]
})
export class FooterModule {
}
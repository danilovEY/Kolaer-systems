import {NgModule} from '@angular/core';
import {FooterComponent} from './footer.component';
import {AppCoreModule} from '../../AppCoreModule';

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

import {NgModule, ModuleWithProviders} from '@angular/core';

import {MdbButtonCheckboxDirective} from './checkbox.directive';
import {MdbButtonRadioDirective} from './radio.directive';

@NgModule({
	declarations: [MdbButtonCheckboxDirective, MdbButtonRadioDirective],
	exports: [MdbButtonCheckboxDirective, MdbButtonRadioDirective]
})
export class MdbButtonsModule {
	public static forRoot(): ModuleWithProviders {
		return {ngModule: MdbButtonsModule, providers: []};
	}
}

import {NgModule, ModuleWithProviders} from '@angular/core';
import {MdbActiveDirective} from './active.class';
import {MdbEqualValidatorDirective} from './equal-validator.directive';
import {MdbInputValidateDirective} from './input-validate.directive';

@NgModule({
	declarations: [MdbActiveDirective, MdbEqualValidatorDirective, MdbInputValidateDirective],
	exports: [MdbActiveDirective, MdbEqualValidatorDirective, MdbInputValidateDirective]
})

export class MdbActiveModule {
	public static forRoot(): ModuleWithProviders {
		return {ngModule: MdbActiveModule, providers: []};
	}
}

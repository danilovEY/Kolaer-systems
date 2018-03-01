import {NgModule, ModuleWithProviders} from '@angular/core';
import {MdbRippleDirective} from './ripple-effect.directive';

@NgModule({
	declarations: [MdbRippleDirective],
	exports: [MdbRippleDirective]
})

export class MdbRippleModule {
	public static forRoot(): ModuleWithProviders {
		return {ngModule: MdbRippleModule, providers: []};
	}
}

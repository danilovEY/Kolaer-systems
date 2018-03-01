import {NgModule, ModuleWithProviders} from '@angular/core';
import {MdbCollapseDirective} from './collapse.directive';

@NgModule({
	declarations: [MdbCollapseDirective],
	exports: [MdbCollapseDirective]
})
export class MdbCollapseModule {
	public static forRoot(): ModuleWithProviders {
		return {ngModule: MdbCollapseModule, providers: []};
	}
}

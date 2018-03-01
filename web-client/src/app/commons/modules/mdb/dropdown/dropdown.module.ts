import {ModuleWithProviders, NgModule} from '@angular/core';

import {MdbBsDropdownContainerComponent} from './dropdown-container.component';
import {MdbBsDropdownMenuDirective} from './dropdown-menu.directive';
import {MdbBsDropdownToggleDirective} from './dropdown-toggle.directive';
import {MdbBsDropdownConfig} from './dropdown.config';

import {MdbBsDropdownDirective} from './dropdown.directive';
import {MdbBsDropdownState} from './dropdown.state';
import {MdbComponentLoaderFactory} from '../utils/component-loader/component-loader.factory';
import {MdbPositioningService} from '../utils/positioning/positioning.service';

@NgModule({
	declarations: [
		MdbBsDropdownMenuDirective,
		MdbBsDropdownToggleDirective,
		MdbBsDropdownContainerComponent,
		MdbBsDropdownDirective
	],
	exports: [
		MdbBsDropdownMenuDirective,
		MdbBsDropdownToggleDirective,
		MdbBsDropdownDirective
	],
	entryComponents: [MdbBsDropdownContainerComponent]
})
export class MdbBsDropdownModule {
	public static forRoot(config?: any): ModuleWithProviders {
		return {
			ngModule: MdbBsDropdownModule, providers: [
				MdbComponentLoaderFactory,
				MdbPositioningService,
				MdbBsDropdownState,
				{provide: MdbBsDropdownConfig, useValue: config ? config : {autoClose: true}}
			]
		};
	};
}

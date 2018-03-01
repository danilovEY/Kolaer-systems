import {NgModule, ModuleWithProviders} from '@angular/core';
import {CommonModule} from '@angular/common';

import {PopoverConfig} from './popover.config';
import {PopoverDirective} from './popover.directive';
import {PopoverContainerComponent} from './popover-container.component';
import {MdbComponentLoaderFactory} from '../utils/component-loader/component-loader.factory';
import {MdbPositioningService} from '../utils/positioning/positioning.service';

@NgModule({
	imports: [CommonModule],
	declarations: [PopoverDirective, PopoverContainerComponent],
	exports: [PopoverDirective],
	entryComponents: [PopoverContainerComponent]
})
export class PopoverModule {
	public static forRoot(): ModuleWithProviders {
		return {
			ngModule: PopoverModule,
			providers: [PopoverConfig, MdbComponentLoaderFactory, MdbPositioningService]
		};
	}
}

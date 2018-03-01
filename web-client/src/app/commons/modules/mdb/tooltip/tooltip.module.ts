import {CommonModule} from '@angular/common';
import {NgModule, ModuleWithProviders} from '@angular/core';
import {TooltipContainerComponent} from './tooltip.component';
import {TooltipDirective} from './tooltip.directive';
import {TooltipConfig} from './tooltip.service';
import {MdbComponentLoaderFactory} from '../utils/component-loader/component-loader.factory';
import {MdbPositioningService} from '../utils/positioning/positioning.service';

@NgModule({
	imports: [CommonModule],
	declarations: [TooltipDirective, TooltipContainerComponent],
	exports: [TooltipDirective],
	entryComponents: [TooltipContainerComponent]
})
export class MDBTooltipModule {
	public static forRoot(): ModuleWithProviders {
		return {
			ngModule: MDBTooltipModule,
			providers: [TooltipConfig, MdbComponentLoaderFactory, MdbPositioningService]
		};
	};
}

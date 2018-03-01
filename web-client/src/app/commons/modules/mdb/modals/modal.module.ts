import {NgModule, ModuleWithProviders, NO_ERRORS_SCHEMA} from '@angular/core';

import {MdbModalBackdropComponent} from './modalBackdrop.component';
import {MdbModalDirective} from './modal.directive';
import {MdbModalContainerComponent} from './modalContainer.component';
import {MdbModalService} from './modal.service';
import {MdbComponentLoaderFactory} from '../utils/component-loader/component-loader.factory';
import {MdbPositioningService} from '../utils/positioning/positioning.service';

@NgModule({
	declarations: [MdbModalBackdropComponent, MdbModalDirective, MdbModalContainerComponent],
	exports: [MdbModalBackdropComponent, MdbModalDirective],
	entryComponents: [MdbModalBackdropComponent, MdbModalContainerComponent],
	schemas: [NO_ERRORS_SCHEMA]
})
export class MdbModalModule {
	public static forRoot(): ModuleWithProviders {
		return {ngModule: MdbModalModule, providers: [MdbModalService, MdbComponentLoaderFactory, MdbPositioningService]};
	}
}

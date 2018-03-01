import {CommonModule} from '@angular/common';
import {NgModule, ModuleWithProviders} from '@angular/core';

import {MdbCarouselComponent} from './carousel.component';
import {MdbSlideComponent} from './slide.component';
import {MdbCarouselConfig} from './carousel.config';

@NgModule({
	imports: [CommonModule],
	declarations: [MdbSlideComponent, MdbCarouselComponent],
	exports: [MdbSlideComponent, MdbCarouselComponent],
	providers: [MdbCarouselConfig]
})
export class MdbCarouselModule {
	public static forRoot(): ModuleWithProviders {
		return {ngModule: MdbCarouselModule, providers: []};
	}
}

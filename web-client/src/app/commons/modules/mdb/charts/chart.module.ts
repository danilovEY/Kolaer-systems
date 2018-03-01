import {NgModule} from '@angular/core';

import {MdbBaseChartDirective} from './chart.directive';

@NgModule({
	declarations: [
		MdbBaseChartDirective
	],
	exports: [
		MdbBaseChartDirective
	],
	imports: []
})
export class MDBChartsModule {
}

import {NgModule} from '@angular/core';
import {TestComponent} from './test.component';
import {TestRoutingModule} from './test.routing';

@NgModule({
	declarations: [
		TestComponent
	],
	imports: [
		TestRoutingModule
	]
})
export class TestModule {
}
